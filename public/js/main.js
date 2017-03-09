$(function() {

  //window.alert('yes');

  $('.js-dropdown').click( function () {
    var $this = $(this);
    $this.closest('li').find('js-dropcontainer').slideToggle(500);
  });

});
