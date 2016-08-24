angular.module('sampleApp', ['ngRoute','ngCookies','ngSanitize','jsonFormatter','chart.js',
 'sampleApp.appRoutes', 
 'sampleApp.HomeCtrl',
 'sampleApp.TrackCtrl',
 'sampleApp.TestCtrl',
 'sampleApp.MsgCtrl',
 'sampleApp.MsgBoardCtrl',
 'sampleApp.AutoCompleteDirective',
 'sampleApp.PopupModelDirective'])
    .run(function() {
}).config(function (ChartJsProvider) {
 ChartJsProvider.setOptions({ colors : [ '#803690', '#949FB1','#4D5360', '#DCDCDC', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#4D5360'] });
});