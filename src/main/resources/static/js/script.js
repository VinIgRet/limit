/**
 * File:        admin/js/script.js
 *
 * @package     Danneo Basis kernel
 * @version     Danneo CMS (Next) 
 * @copyright   (c) 2005-2017 Danneo Team
 * @link        http://danneo.ru
 * @license     http://www.gnu.org/licenses/gpl-2.0.html
 */
// No Script
$('html').addClass('js');
$(function(){
	$('img, a, input, div, td, p').tooltip();
	$('#reload').reload();
	$('.menu-toggle').on('click',function(){
		$('.aside').toggle();
		if($('.aside').is(':hidden')) {
			cookie.set('menup','closed',{path:$.apanel});
			$('#menup').attr('src',$.apanel+'template/skin/'+$.template+'/images/open.gif');
		}else{
			cookie.set('menup','open',{path:$.apanel});
			$('#menup').attr('src',$.apanel+'template/skin/'+$.template+'/images/closed.gif');
		}
	});

	//$('a.window-box, a.all-comments').colorbox({
	//	width:'92%',height:'90%',maxHeight:800,maxWidth:1200,initialWidth:800,initialHeight:600,fixed:true,onComplete:function(){var h=$('#cboxLoadedContent').height();$('#fb-work-comm').css({'height':(h-162)+'px'});}
	//});
	//$('textarea:not(.noresize)').TextAreaResizer();
	

	$('#aside').on('click','span,a',function(){
		let pb=$(this).parent(),id=pb.attr('id');
		if($(this).is('a')){
			if(id){cookie.set('openmenu',id,{expires:7,path:jQuery.apanel});}			
		}else{
			if(pb.hasClass('pmh')){
				if(pb.hasClass('open')){
					pb.next().slideUp();
					pb.removeClass('open');
					cookie.set('openmenu','',{expires:7,path:jQuery.apanel});
				}else{
					if(id){cookie.set('openmenu',id,{expires:7,path:jQuery.apanel});}
					$('.pmh').removeClass('open');
					$('.pmc').slideUp();
					pb.addClass('open');
					pb.next().slideDown();
				}
			}else if(pb.hasClass('bmh')){
				if(pb.hasClass('open')){
					pb.next().slideUp();
					pb.removeClass('open');
				}else{
					$('.bmh').removeClass('open');
					$('.bmc').slideUp();
					pb.addClass('open');
					pb.next().slideDown();
				}			
			}
		}
	});
	$(window).on('load',function(){
		$('#'+cookie('openmenu')).addClass('open');
		let pb=$('#'+cookie('openmenu')).closest('bmh');
		pb.addClass('open');pb.next().slideDown();
	});



	
	$('form').on('focus','.flxinp>button,.flxinp>a,.flxinp>input,.flxinp>select',function(){
		$(this).parent().addClass('focus');
	}).on('blur','.flxinp>button,.flxinp>a,.flxinp>input,.flxinp>select',function(){
		$(this).parent().removeClass('focus');			
	});
	$('form').on('click','.flxinp>a',function(){
		$(this).parent().find('input').focus();
	});
	var panel=$('.tab-menu');
	if(panel.length){
		$('.handle').on('click',function(){
			if(panel.hasClass('visible')){
				panel.animate({right:'-=175',},200,function(){$(this).removeClass('visible');});
			}else{
				panel.animate({right:'+=175',},200,function(){$(this).addClass('visible');$(".tab-menu > p").css({'display':'block'});});
			}
		});
	};
	$(window).on('click',function(e){
		if($(e.target).closest(".tab-menu").length){return;}
		if($(".tab-menu").hasClass('visible')){
			$(".tab-menu").animate({right:'-=175',},200,function(){
				$(this).removeClass('visible');
				$(this).css({'right':'-175px'});
				$(".tab-menu > p").css({'display':'none'});
			});
		};
		e.stopPropagation();
	});
	$('.sel-plat > select').each(function(){$(this).siblings('legend').text($(this).children('option:selected').text());});
	$('.sel-plat > select').change(function(){$(this).siblings('legend').text($(this).children('option:selected').text());});
	$('#acc').bind('change',function(){if($(this).val()=='group'){$('#group').slideDown();}else{$('#group').slideUp();}});
	$("#checkboxall").click(function(){
		var check=this.checked;
		$("input[type=checkbox]").each(function(){this.checked=check;});
	});
});
function globalnotice(title,description,url_image,class_name){
	var unique_id=$.gritter.add({title:title,text:description,image:url_image,sticky:true,time:'',iclass:class_name});
}
(function($){
	$.fn.reload=function(){
		$(this).click(function(){
			reload(this);
			return true;
		});
		function reload(){
			$('body').append('<div id="overlay" />');
			$('body').css({height:'100%'});
			$('#overlay').css({display:'none',position:'absolute',top:0,left:0,width:'100%',height:'100%',zIndex:1000,background:'black',opacity:1}).fadeIn(400);
			$('#overlay').fadeOut(400);
		}
	};
})(jQuery);
function loads(){
	$('#lds').show();
	$('#lds').html('<div class="save"></div>');
	//$('#lds').fadeOut(3000);
}
$(document).mouseup(function(e){
	if(!$("#lds").is(e.target)&&$("#lds").has(e.target).length===0){$("#lds").hide();}
});