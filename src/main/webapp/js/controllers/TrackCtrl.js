angular.module('sampleApp.TrackCtrl', []).controller('TrackController', function($cookieStore, $location, $scope, $http) {

    if(!$cookieStore.get('isLoggedIn')){
        $location.path('/');
    }

    $scope.myModel = null;
    $scope.myModelId = null;

    $scope.entityList = [];
    $scope.showTable = false;

    $scope.fetchRecords = function(text){
        var url = '/find';
        if(text){
            url += '/'+text;
        }
        $http.get(url).success( function(data) {
            $scope.entityList = data;
            if($scope.entityList.length>0){
                $scope.showTable = true;
            }else{
                $scope.showTable = false;
            }
        });
    };

    $scope.component = undefined;
    $scope.showModal = false;
    $scope.toggleModal = function(entity){
        $scope.component = entity.component;
        $scope.showModal = !$scope.showModal;
    };

});