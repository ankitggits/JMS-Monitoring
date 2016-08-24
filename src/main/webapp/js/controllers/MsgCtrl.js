angular.module('sampleApp.MsgCtrl', []).controller('MessageController', function($cookieStore, $location, $scope) {

    if(!$cookieStore.get('isLoggedIn')){
        $location.path('/');
    }

    if(!$cookieStore.get('selectedDomain')){
        $location.path('/msgBoardPage');
    }else{
        $scope.selectedDomain = $cookieStore.get('selectedDomain');
    }
    
    $scope.messageList = [];

    $scope.message = undefined;

    $scope.component = undefined;

    $scope.searchKeyword = undefined;

     var refresh = function() {

        if (!!window.EventSource) {
            console.log("Event source available");
            var source = new EventSource('/emitter/open');

            source.addEventListener('message', function(e) {
                $scope.$apply(function () {
                    var msg = JSON.parse( e.data );
                    $scope.message = msg;
                    $scope.messageList.splice(0, 0, msg);
                    /*$scope.messageList.push(JSON.parse( e.data ));*/
                });
            });

            source.addEventListener('open', function(e) {
                console.log("Connection was opened.");
            }, false);

            source.addEventListener('error', function(e) {
                if (e.readyState == EventSource.CLOSED) {
                    console.log("Connection was closed.");
                } else {
                    console.log(e.readyState);
                }
            }, false);
        } else {
            console.log("No SSE available");
        }
    };

    refresh();

    $scope.component = undefined;
    $scope.showModal = false;
    $scope.toggleModal = function(message){
        $scope.component = message.component;
        $scope.showModal = !$scope.showModal;
    };

});
