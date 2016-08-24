angular.module('sampleApp.HomeCtrl', []).controller('HomeController', function($location, $scope, $http, $cookieStore) {

	$scope.hasError=false;
	$scope.errorMessage="";

	$scope.loginUser = function() {
	  console.log($scope.loginDetail);
	  if($scope.loginDetail==undefined || $scope.loginDetail.username==undefined || $scope.loginDetail.password==undefined){
      	$scope.errorMessage="invalid input";
      	$scope.hasError=true;
      }else{
		  $http.post('/login',$scope.loginDetail)
			  .success(function (data, status, headers) {
				  $scope.hasError=false;
				  $scope.errorMessage="";
				  $location.path('/trackPage');
				  $cookieStore.put('isLoggedIn', true);
				  $cookieStore.put('isAdmin', true);
			  })
			  .error(function (data, status, header, config) {
				  $scope.errorMessage="invalid input";
				  $scope.hasError=true;
			  });
      }
	};

	$scope.logoutUser = function() {
		$http.get('/logout');
		$scope.errorMessage="";
		$scope.hasError=false;
		$cookieStore.put('isLoggedIn', false);
		$cookieStore.put('isAdmin', false);
		$cookieStore.put('loggedUserName', undefined);
		$location.path('/');
	};

	$scope.clearMsg = function() {
		$scope.hasError=false;
		$scope.errorMessage="";
	};
	
	$scope.isLoggedInFun = function() {
		$scope.loggedUserName = $cookieStore.get('loggedUserName');
		return $cookieStore.get('isLoggedIn');
	};
	
	$scope.isAdminFun = function() {
		return $cookieStore.get('isAdmin');
	};
	

});