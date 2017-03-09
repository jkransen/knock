$(function() {

  // show/hide more content in delivery-list

  $('.delivery-address--more-content').hide();

  $('.js-dropdown').click( function () {
    var $this = $(this);
    $this.closest('li').siblings().find('.delivery-address--more-content').slideUp();
    $this.closest('li').find('.delivery-address--more-content').slideToggle(500);
  });


  $('.modal').hide();

  $('.delivery-toolbar__notin button').click ( function () {
    $('.modal').show();
  });

  $('.modal').click(
    function(e)
    {
      if(e.target.className !== "modal__content")
      {
        $(".modal").hide();
      }
    }
  );

});
