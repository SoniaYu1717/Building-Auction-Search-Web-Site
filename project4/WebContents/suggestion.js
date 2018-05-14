
// StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl,bTypeAhead) {
//     var aSuggestions = [];
//     var sTextboxValue = oAutoSuggestControl.textbox.value;

//     if (sTextboxValue.length > 0){

//         for (var i=0; i < this.states.length; i++) { 
//             if (this.states[i].indexOf(sTextboxValue) == 0) {
//                 aSuggestions.push(this.states[i]);
//             } 
//         }
//     }

//     oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
// };

// function SuggestionProvider() {
//     //any initializations needed go here
// }

// SuggestionProvider.prototype.requestSuggestions = function (oAutoSuggestControl) {

//     var aSuggestions = new Array();

//     //determine suggestions for the control
//     oAutoSuggestControl.autosuggest(aSuggestions);
// };


var xhr = new XMLHttpRequest();

function StateSuggestions() {}

StateSuggestions.prototype.sendAjaxRequest = function (oAutoSuggestControl, bTypeAhead) {
   var sTextboxValue = oAutoSuggestControl.textbox.value;
       var request = "/eBay/suggest?q=" + encodeURI(sTextboxValue);
       xhr.open("GET", request);
       xhr.onreadystatechange = this.requestSuggestions(oAutoSuggestControl, bTypeAhead);
       xhr.send(null);

};

StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl, bTypeAhead) {

    var aSuggestions = [];
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    var request = "/eBay/suggest?q=" + encodeURI(sTextboxValue);
    xhr.open("GET", request);
    xhr.onreadystatechange = function() {
         if (xhr.readyState == 4) {
             var s = xhr.responseXML.getElementsByTagName('CompleteSuggestion');

             for (var i=0; i < s.length; i++) {
                 var text = s[i].childNodes[0].getAttribute("data");
                 aSuggestions.push(text);
             }
         }

         oAutoSuggestControl.autosuggest(aSuggestions, false);
    }

    xhr.send(null);

};