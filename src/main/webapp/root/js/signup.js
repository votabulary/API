$(document).ready(function(){
  $("#contactform").submit(function(e) {
    var postData = $(this).serializeArray();
    alert(postData);
    e.preventDefault(); //STOP default action
//    e.unbind(); //unbind. to stop multiple form submit.
  });
  $("#btnSubmit").click(function(){
    $("#contactform").submit();
  });
});