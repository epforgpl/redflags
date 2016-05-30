$(function() {
	$("h2[id],h3[id]").each(function() {
		$(this).append('<small><a class="top-link" href="#"><i class="fa fa-angle-up"></i></a></small>');
	});
	$("li.dropdown-submenu:has(li.active)").addClass('active');
});