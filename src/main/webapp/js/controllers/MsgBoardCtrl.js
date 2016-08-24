angular.module('sampleApp.MsgBoardCtrl', []).controller('MessageBoardController', function($cookieStore, $location, $scope) {


    if(!$cookieStore.get('isLoggedIn')){
        $location.path('/');
    }

    $scope.domainList = [];

     var refresh = function() {
         $scope.domainList = ['PAYMENT', 'CUSTOMER', 'MERCHANT'];
    };

    $scope.domainBoard = function(domain){
        $cookieStore.put('selectedDomain', domain);
        $location.path('/msgPage');
    };

    refresh();

});
