angular.module('sampleApp.AutoCompleteDirective', [])
    .directive('autoComplete', function($http) {

        return function (scope, element, attrs) {
            element.autocomplete({
                minLength:1,
                source:function (request, response) {
                    var url = '/suggest/'+request.term;
                    console.log('url is'+url);
                    $http.get(url).success( function(data) {
                        response(data);
                    });
                },
                focus:function (event, ui) {
                    element.val(ui.item.label);
                    return false;
                },
                select:function (event, ui) {
                    scope.$apply(function() {
                        scope.myModel = ui.item.value;
                    });
                    return false;
                },
                change:function (event, ui) {
                    if (ui.item === null) {
                        scope.myModel = null;
                    }
                }
            }).data("ui-autocomplete")._renderItem = function (ul, item) {
                return $("<li></li>")
                    .data("item.autocomplete", item)
                    .append("<a>" + item.label + "</a>")
                    .appendTo(ul);
            };
        }
});