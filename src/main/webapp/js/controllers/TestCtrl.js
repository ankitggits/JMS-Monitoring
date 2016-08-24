angular.module('sampleApp.TestCtrl', ['angular-svg-round-progressbar'])
    
    .controller('TestController', function( $http, $cookieStore, $location, $scope, $timeout, $window, ChartJs ) {

    var checkedPrefs = [];
    var plots = [];
    $scope.switched=true;
    $scope.performanceResps =[];
    $scope.onClick = function (points, evt) {};
    $scope.showPlot = false;
    $scope.graphError= undefined;
    $scope.showButtons = false;
    $scope.master = false;
    $scope.showModal = false;
    $scope.envErrorMsg = undefined;

    $scope.domainList = [];
    for (var k in DomainURL){
        $scope.domainList.push(k);
    }

    $scope.source=$scope.domainList[0];
    $scope.destination=$scope.domainList[0];

    $scope.environments = ['-------------SELECT ENV-------------'];
    for (var k in ServerEnv){
        $scope.environments.push(k);
    }

    $scope.env= $scope.environments[0];
    $scope.performance = {
        requests : 1,
        concurrency : 1,
        url:"",
        msg:"",
        listenerConcurrency:undefined
    };

    $scope.evaluateUrl = function(){
        $scope.performance.url = ServerEnv[$scope.env]+Port[$scope.source]+DomainURL[$scope.source]+PingURL[$scope.destination];
        if($scope.env!=$scope.environments[0]){
            $scope.envErrorMsg=undefined;
            getListenerConcurrency();
        }else{
            $scope.performance.sourceListenerConcurrency=undefined;
            $scope.performance.destListenerConcurrency=undefined;
            $scope.performance.url=undefined;
        }
    };

    var getListenerConcurrency = function(){
        $http.post('/listener/concurrency', {source:ServerEnv[$scope.env]+Port[$scope.source]+DomainURL[$scope.source]+ListenerURL,destination:ServerEnv[$scope.env]+Port[$scope.destination]+DomainURL[$scope.destination]+ListenerURL})
            .success(function (data, status, headers) {
                $scope.performance.sourceListenerConcurrency=data.source.domainListenerConcurrency;
                $scope.performance.destListenerConcurrency=data.destination.specificListenerConcurrency;
            })
            .error(function (data, status, header, config) {

            });
    };

    var updateTestResult = function(){
        $http.get('/performance').success(function (data, status, headers) {
            $scope.performanceResps = data;
        })
    };

    updateTestResult();

    $scope.switchChanged = function(){
          var started = !$scope.switched;
          if(started){
              if($scope.env===$scope.environments[0]){
                  $scope.envErrorMsg="please select env";
                  $scope.switched=true;
              }else{
                  $scope.envErrorMsg=undefined;
                  $http.post('/performance',$scope.performance)
                      .success(function (data, status, headers) {
                          $scope.showPlot = false;
                          $scope.showButtons = false;
                          $scope.performanceResp = data;
                          $scope.master = false;
                          $scope.graphError= undefined;
                          $scope.performanceResps.unshift(data);
                          $scope.switched=true;
                          $(window).scrollTop(400);
                      })
                      .error(function (data, status, header, config) {});
              }
          }else{}
     };

    var addToPlot = function(value){
        var perf = value;
        var id = perf.requests+'/'+perf.concurrency;
        var isPresent = false;
        angular.forEach(plots, function(value, key) {
            if(value.id === id){
                value.performances.push(perf);
                isPresent = true;
            }
        });
        if(!isPresent){
            plots.push({id: id, performances: [perf]});
            $scope.series.push(id);
        }
    };

    var removeFromPlot = function(pref){
        var id = perf.requests+'/'+perf.concurrency;
        angular.forEach(plots, function(value, key) {
            if(value.id === id){
                if(value.performances.length===1){
                    var index = plots.indexOf(value);
                    plots.splice(index, 1);
                }else{
                    angular.forEach(value.performances, function(intValue, intKey){
                        if(intValue.completedIn === perf.completedIn){
                            value.performances.splice(intKey, 1);
                        }
                    });
                }
            }
        });
    };

    $scope.doPlot = function(){
         plots  = [];
         $scope.labels = ['10','20','30'];
         $scope.series = [];
         $scope.data = [[]];
         angular.forEach(checkedPrefs, function(value, key) {
            addToPlot(value);
         });

         angular.forEach(plots, function(value, key) {
                  if($scope.series.indexOf(value.id)<0){
                      $scope.series.push(value.id);
                  }
                  angular.forEach(value.performances, function(intValue, intKey) {
                      var xAxis = intValue.sourceListenerConcurrency+"-"+intValue.destListenerConcurrency;
                      /*if($scope.labels.indexOf(xAxis)== -1){
                          $scope.labels.push(xAxis);
                      }*/
                      var seriesIndex = $scope.series.indexOf(value.id);
                      if(!$scope.data[seriesIndex]){
                          $scope.data[seriesIndex] = [intValue.completedIn];
                      }else if($scope.data[seriesIndex].indexOf(intValue.completedIn)<0){
                          $scope.data[seriesIndex].push(intValue.completedIn);
                      }
                  });
         });
          /*if($scope.labels.length!=$scope.data[0].length){
              $scope.graphError = "You have selected same listeners result";
              $scope.showPlot = false;
          }else{
              $scope.graphError = undefined;
              $scope.showPlot = true;
          }*/
         $scope.showPlot = true;
    };

    $scope.doDelete = function(){
        angular.forEach(checkedPrefs, function(value, key) {
            $http.delete('performance/'+value.performanceId)
                .success(function (data, status, headers) {
                    checkedPrefs.splice(key,1);
                    $scope.performanceResps.splice($scope.performanceResps.indexOf(value),1);
                })
                .error(function (data, status, header, config) {
                    $scope.graphError = "Items not deleted properly";
                });
        });
    };

    $scope.checkChange = function(perf, event) {
        if(event.currentTarget.checked){
             var notPresent = true;
             angular.forEach(checkedPrefs, function(value, key) {
                 if(value.performanceId===perf.performanceId){
                     notPresent = false;
                 }
             });
             if(notPresent){
                 checkedPrefs.push(perf);
             }
        } else {
            angular.forEach(checkedPrefs, function(value, key) {
                if(value.performanceId===perf.performanceId){
                    checkedPrefs.splice(key, 1);
                }
            });
        }
        if(checkedPrefs.length>0){
            $scope.showButtons = true;
        }else{
            $scope.showButtons = false;
        }
    };

    $scope.selectAllCLicked = function(event){
         checkedPrefs = [];
         if(event.currentTarget.checked) {
             angular.forEach($scope.performanceResps, function(value, key) {
                 checkedPrefs.push(value);
             });
             $scope.showButtons = true;
         }else{
             checkedPrefs = [];
             $scope.showButtons = false;
             $scope.showPlot = false;
         }
    };

    $scope.extras = {
             Request_Send_Rec : { min : 0, max : 0},
             Service_Execution : { min : 0, max : 0},
             Response_Send_Rec : { min : 0, max : 0},
             Messaging_Only : { min : 0, max : 0},
             Rest:{ min : 0,  max : 0}
    };

    $scope.toggleModal = function(performanceResp){
          $scope.extras.Request_Send_Rec = performanceResp.reqSendRec;
          $scope.extras.Service_Execution = performanceResp.serviceExe;
          $scope.extras.Response_Send_Rec = performanceResp.resSendRec;
          $scope.extras.Messaging_Only = performanceResp.messaging;
          $scope.extras.Rest = performanceResp.rest;
          $scope.showModal = !$scope.showModal;
    };

});
