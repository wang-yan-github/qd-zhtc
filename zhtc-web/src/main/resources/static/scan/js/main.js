$(function(){	
  $('#member').on('click', function() {
        location.href='messgaelist.html';
  });
  $('#rebate').on('click', function() {
        location.href='orderlist.html';
  });
  $('#home').on('click', function() {
        location.href='main.html';
  });
  $('#aboutus').on('click', function() {
      location.href='usercenter.html';
});
  $('.btn-left').on('click', function() {
  	if (!$(this).hasClass('homeset')) {history.back();}else{location.href='index.html';}
        
  });
  $('.btn-right').on('click', function() {
        location.href='index.html';
  });
  $('#userset').on('click', function() {
      location.href='usermana.html';
});

})