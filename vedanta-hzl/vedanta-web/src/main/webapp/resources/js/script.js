$(document).ready(function () {
	$('body').addClass('fixed-sidebar');
    // Add body-small class if window less than 768px
    
	if ($(this).width() <= 767) {
        $('body').addClass('body-small')
    } else {
        $('body').removeClass('body-small')
    }
    // MetsiMenu
    $('#side-menu').metisMenu();    
    
    $("form").attr("autocomplete","off");
    
    /*Minimalize menu*/
    $('.navbar-minimalize').click(function () {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });
    var flag=0;
    $('.page-wrapper').on('click', function () {
    	if($('body').hasClass('mini-navbar')){
    		if(flag == 0)
    			flag = 1;
    		else{
    			$("body").removeClass("mini-navbar");
    		       SmoothlyMenu();
    			flag= 0;
    		}
    	}
    });
    
});

/* Minimalize menu when screen is less than 768px*/
$(window).bind("resize", function () {
    if ($(this).width() <= 767) {
        $('body').addClass('body-small')
    } else {
        $('body').removeClass('body-small')
    }
});


// For demo purpose - animation css script
function animationHover(element, animation) {
    element = $(element);
    element.hover(
	function () {
	    element.addClass('animated ' + animation);
	},
	function () {
	    //wait for animation to finish before removing classes
	    window.setTimeout(function () {
	        element.removeClass('animated ' + animation);
	    }, 2000);
	});
}
function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
        // Hide menu in order to smoothly turn on when maximize menu
        $('#side-menu').hide();
        // For smoothly turn on menu
        setTimeout(
            function () {
            	
                $('#side-menu').fadeIn(500);
            }, 100);
    } else if ($('body').hasClass('fixed-sidebar')) {
        $('#side-menu').hide();
        setTimeout(
            function () {
            	alert('ajkfhsjkdh')
                $('#side-menu').fadeIn(500);
            }, 300);
    } else {
        // Remove all inline style from jquery fadeIn function to reset menu state
        $('#side-menu').removeAttr('style');
    }
}
