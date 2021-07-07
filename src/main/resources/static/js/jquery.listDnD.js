!function($,window,document,undefined){
var startEvent='touchstart.listDnD mousedown.listDnD',moveEvent='touchmove.listDnD mousemove.listDnD',endEvent='touchend.listDnD mouseup.listDnD';
jQuery.listDnD={
    currentTable:null,
		dragObject:null,
		mouseOffset:null,
		tables:[],
		oldY:0,
		cbx:false,
		currentContMenu:null,
		build:function(options){
			var IndSet=!options.indSet?{}:options.indSet;
			delete options.indSet;
			this.each(function(){
				let individual=(!!IndSet[this.id])?IndSet[this.id]:{};
				this.listDnDConfig=$.extend(true,{
					ajax:true,
					isDragable:false,
					isSortable:false,
					isNumerable:false,
					pagination:false,
					multySelect:false,
					ierList:false,
					toolBar:false,
					elTrClass:"list",
 					dragHandle:null,
					onselClass:"select",
					onhoverClass:"hover",
					onDragClass:"hover",
					onAllowDrop:null,
					onDrop:null,
					onDragStart:null,
					onDragStop:null,
					menuImgPath:"",
					contMenuList:[],
					actionLink:{},
					contMenuListSys:{chkall:{label:"Выделить все"},unchkall:{label:"Снять выделение"},chkrev:{label:"Обратить выделение"}},
					keybList:{enter:'edit',delete:'del',insert:'add',space:'chk'},
					scrollAmount:48,
					sensitivity:12,
					jsonPretifySeparator:'\t',
					serializeRegexp:/[0-9]+/,
					serializeParamName:"list",
					rangeAnchor:false,
					rangeEndpoint:false,
					countSel:0,
					countList:0,
					contMenu:0
				},options||{},individual);
				this.listDnDConfig.pagination&&$.listDnD.makePagination(this);
				this.listDnDConfig.isSortable&&$.listDnD.makeSortable(this);
				$.listDnD.makeDraggable(this);
				!$.isEmptyObject(this.listDnDConfig.contMenuList)&&$.listDnD.createContMenu(this);
				$.listDnD.makeSelectable(this);
				$.listDnD.tables.push(this);
			});
			$(document).on(startEvent,function(e){
				if(!$(e.target).closest($($.listDnD.tables)).length&&!$(e.target).closest($(".listitems"+((!!$.listDnD.currentTable)?$.listDnD.currentTable.listDnDConfig.contMenu:"0000"))).length){
					if($($.listDnD.currentTable).length===1){
						$.listDnD.currentTable=null;
						$.listDnD.dragObject=null;
						$($.listDnD.tables).removeClass("focuslist");
					}
					$(document).off("keydown.listDnD").off("keyup.listDnD").off("contextmenu.listDnD");
					$.listDnD.keyEventSet=false;
					$.listDnD.closeContMenu();
				}
			});
			return this;
    	},
		extLinkListUpdate:function(table,elm){
			let lnk='',p=$(table).find(".listpags a.pages.selpag").data("p"),nu=$(table).find(".amount a.pages.selpag").data("nu"),sk=$("#"+table.id+" th.selsort.sortcol");
			if(!!elm){
				if($(elm).hasClass("pages")){
					if($(elm).closest(".listpags").length>0){lnk+="?p="+$(elm).data("p");}else{lnk+="?n="+$(elm).data("n");}
					if(sk.length){lnk+="&s="+sk.data("s")+"&l="+((sk.data("l")==="asc")?"desc":"asc");}
				}else{
					lnk+="?p="+p+"&s="+$(elm).data("s")+"&l="+$(elm).data("l");
				}
			}else{
				lnk+=((!!p)?"?p="+p:"")+((sk.length)?"&s="+sk.data("s")+"&l="+((sk.data("l")==="asc")?"desc":"asc"):"");
			}
			return lnk;
		},
		repaintList:function(table,elm,data,full=true){
			let config=table.listDnDConfig;
			$("#list").html(data);
			config.countList=$.listDnD.filter_rows(table,config).length;
			if(config.toolBar){
				var pnl=$("#contmenu"+config.contMenu).clone(true).addClass('contpanel').removeClass('contmenu').removeAttr("id");
				var hdr = $(table).find('tr.head-cntrl td:first');
				hdr.prepend(pnl);
				$(pnl).find('a').addClass('side-button');
				$(pnl).on("click.listDnD","a",this.clickInMenu);
			}
			$.listDnD.act_deact_contMenu(table,config);
			$.listDnD.closeContMenu();
			!!this.cbx&&$.colorbox.close();
		},
		dataListUpdate:function(table,elm){
			let config=table.listDnDConfig,exlnk=$.listDnD.extLinkListUpdate(table,elm);
			let lnk=$(table).data("listlink")+(!!exlnk?exlnk:"");
			if(config.ajax){
				$.listDnD.ajaxAction(table,lnk,null,"get").done(function(data){$.listDnD.repaintList(table,elm,data,true);});
			}else{
				window.location=lnk;
			}
		},
		infoBox:function(act,data=null){
			if(act=='close'){
				if(!!this.cbx){
					if(!data){
						$.colorbox.close();
						$("#ajaxask").hide();
					}else{
						let tmp=parseInt(data);
						if(isNaN(tmp)){tmp=1000;}
						setTimeout(function(){$.colorbox.close();$("#ajaxask").hide();},tmp);
					}
					this.cbx=null;
				}
			}else{
				if($("#ajaxask").length==0){var div=document.createElement("div");$(div).html("&nbsp;").attr("id","ajaxask").appendTo("body");}
				if(act=='load'){
					$("#ajaxask").html("&nbsp;");
				}else	if(act=='info'){
						$("#ajaxask").html('<table id="arr-work" class="fb-work"><caption>'+((!!data.hdr)?data.hdr:'Обратите внимание')+'</caption><tbody><tr><td class="cont"><strong>'+((!!data.txt)?data.txt:'Нечего сообщить Вам')+'</strong></td></tbody></table>');
				}else if(act=='html'){
					$("#ajaxask").html(data);
				}
				$("#ajaxask").show();
				if(!this.cbx){
					let h=(act=='load')?"80px":$("#ajaxask").height()+20+"px",w=(act=='load')?"80px":"470px";
					$.colorbox({onClosed:function(){$.listDnD.cbx=null;$("#ajaxask").html("&nbsp;");$("#ajaxask").hide();},overlayClose:false,opacity:"0.005",initialWidth:"50px",initialHeight:"50px",width:w,height:h,maxWidth:"80%",maxHeight:"80%",inline:true,href:"#ajaxask"});
				}
				if(act=='load'){
					$("#ajaxask").css({widht:"60px",height:"60px",background:"url("+$.apanel+"/js/colorbox/images/loading.gif) center center no-repeat"});
					$("#cboxClose").hide();
					!!this.cbx&&$.colorbox.resize({width:"80px",height:"80px"});
				}else{
					$("#ajaxask").css({widht:"450px",height:"auto",background:"none"});
					$("#cboxClose").show();
					!!this.cbx&&$.colorbox.resize({width:"470px"});
				}					
				this.cbx=act;
			}
		},
		ajaxAction:function(table,url,data,method){
			var d=$.Deferred(),p=d.promise();
			$.listDnD.infoBox('load');
			var r=$.ajax({
				url:url,
				data:data,
				processData:false,
				type:method,
				async:true,
				contentType:false,
				dataType:"html"
			}).done(function(data){
				d.resolve(data);
			}).fail(function(data){
				$.listDnD.infoBox('info',data);
				d.reject(data);
			});
			p.abort=function(){return r.abort.apply(r,arguments);}
			return p;
		},
		afterAction:function(table,data){

			if(!!data){
				/*
				if(data.typ=="ok"){
					if(!!data.update){
						if(data.update=='class'&&(!!data.addcls||data.delcls)&&!!data.ids){
							data.ids.forEach(function(item){$("#"+table.id+"_"+item).addClass(data.addcls).removeClass(data.delcls);});
						}else if(data.update=='cell'&&!!data.html&&!!data.ids&&!!data.cell){
							data.ids.forEach(function(item){
								$("#"+table.id+"_"+item).find(data.cell).html(data.html);
							});							
						}else if(data.update=='list'){
							$.listDnD.dataListUpdate(table,null);
						}
					}
					if(!!data.info){
						$.listDnD.infoBox('info',{hdr:"Информация",txt:data.info});
						$.listDnD.infoBox('close',1000);
					}else{
						$.listDnD.infoBox('close');
					}
				}else{
					$.listDnD.infoBox('close');
				}
				*/
				$.listDnD.repaintList(table,null,data,true);
			}else{
				$.listDnD.infoBox('close');
			}
			$.listDnD.act_deact_contMenu(table,table.listDnDConfig);
		},
		chkSelect:function(table,selrow){
			$(table).find("input.chkselect").prop("checked",false);
			selrow.find("input.chkselect").prop("checked",true);
		},
		runAction:function(table,config,actname){
			$.listDnD.filter_rows(table,config,".no_"+actname).removeClass(config.onselClass);
			let selrow=$.listDnD.filter_rows(table,config,"."+config.onselClass);//,extlink=$.listDnD.extLinkListUpdate(table);
			$.listDnD.chkSelect(table,selrow);
			let lnk=config.actionLink[actname].lnk;
			if(!!config.actionLink[actname].data){
				if(selrow.length==0){return;}
				lnk+=$(selrow[0]).data(config.actionLink[actname].data);
			}
			//lnk+=$.listDnD.extLinkListUpdate(table);
			if(!!config.actionLink[actname].ask){
				$("#action").val("ask");
			}
			let frm=new FormData($(table).closest("form")[0]);
			if(config.ajax&&config.actionLink[actname].ajax){
				let met = !!config.actionLink[actname].ask ? "post" : config.actionLink[actname].method;
				$.listDnD.ajaxAction(table,lnk,frm,met).done(function(data){
					if(!!config.actionLink[actname].ask){
						$.listDnD.infoBox('html',data);
						$("#ajaxask").off("submit.listDnD").on("submit.listDnD","form",function(e){
							e&&e.preventDefault();e.stopPropagation();
							let frm=new FormData($("#ajaxask form")[0]),formact=$("#ajaxask form").attr("action");
							$.listDnD.ajaxAction(table,formact,frm,config.actionLink[actname].method).done(function(data){
								$.listDnD.afterAction(table,data);
							});
						});	
					}else{
						$.listDnD.afterAction(table,data);
					}
				});
			}else{
				if (config.actionLink[actname].method == "get") {
					location.replace(lnk);
				} else {
					let form = document.createElement('form');
					form.setAttribute('action', lnk);
					form.setAttribute('method', config.actionLink[actname].method);
					for (let pair of frm.entries()) {
						let input = document.createElement('input');
						input.setAttribute('type', 'text');
						input.setAttribute('name', pair[0]);
						input.setAttribute('value', pair[1]);
						form.appendChild(input);
					}
					document.body.appendChild(form);
					form.submit();
				}
			}
		},
		makePagination:function(table){
			$(table).on("click.listDnD",".pages",function(e){
				e&&e.preventDefault();e.stopPropagation();
				$.listDnD.dataListUpdate(table,e.target);
			});
		},
		makeSortable:function(table){
			$(table).on("click.listDnD",".work-sort,.work-desc-sort,.work-asc-sort",function(e){
				e&&e.preventDefault();e.stopPropagation();
				$.listDnD.dataListUpdate(table,e.target);
			});
		},
		makeSelectable:function(table){
			var config=table.listDnDConfig;
			$(table).on(startEvent,function(e){
				e&&e.preventDefault();e.stopPropagation();
				let tt=$(e.target).closest($($.listDnD.tables));
				if(tt.length==1){tt.addClass("focuslist");$.listDnD.currentTable=tt[0];}
			});
			config.countList=this.filter_rows(table,config).length;
			$(table).on("click.listDnD","tr",this.mouseclick).on("dblclick.listDnD","tr."+config.elTrClass,this.mousedblclick);
			$(table).on("mouseenter.listDnD","tr."+config.elTrClass,function(){$(this).addClass(config.onhoverClass);}).on("mouseleave.listDnD","tr."+config.elTrClass,function(){$(this).removeClass(config.onhoverClass);});
			$(table).on("contextmenu.listDnD","tr",this.contMenuAct);
			!!config.contMenu&&$('.listitems'+config.contMenu).on("click.listDnD",".sys_sel",this.table_select);
		},
		makeDraggable:function(table){
			let config=table.listDnDConfig;
			$(table).on(startEvent,"tr."+config.elTrClass,function(e){
				e.stopPropagation();
				((e.target.tagName==="TD")||(e.target.closest('td')))&&$.listDnD.initialiseSelect($(e.target).closest("tr."+config.elTrClass)[0],table);
				if(config.isDragable){
					if(((config.dragHandle&&$(e.target).is("."+config.dragHandle))||(!config.dragHandle&&e.target.tagName==="TD"))&&!$(e.target).closest("tr."+config.elTrClass).hasClass("nodrag")){
						$.listDnD.initialiseDrag($(e.target).closest("tr."+config.elTrClass)[0],table,e.target,e,config);return false;
					}
				}
				return false;
			});
		},
		positUpList:function(table,config,droprow){
			let pid=$(droprow).data("pid");
			if(pid==$(table).data("lstp")){
				config.ierList&&$.listDnD.repositIerTable(table,config);
			}else{
				config.ierList&&$.listDnD.repositLevel(table,config,droprow/*$(table).find("#"+table.id+"_"+pid)[0]*/);
			}
			config.isNumerable&&$.listDnD.renumTable(table,config);
			if(!!config.actionLink['positup']){
				let presel=$.listDnD.filter_rows(table,config,"."+config.onselClass),a=$(droprow).data("pid"),tmprow=$.listDnD.filter_rows(table,config,".lstp_"+a);
				$.listDnD.chkSelect(table,tmprow);
				let lnk=config.actionLink['positup'].lnk+$.listDnD.extLinkListUpdate(table)+"&sf="+$(table).data("sf");
				if(!!config.actionLink['positup'].data){lnk+=$(droprow).data(config.actionLink['positup'].data);}
				let frm=new FormData($(table).closest("form")[0]);
				$.listDnD.ajaxAction(table,lnk+"&ajax=1",frm,"get").done(function(data){
					!!$.listDnD.cbx&&$.colorbox.close();
				});
				$.listDnD.chkSelect(table,presel);
			}
		},
		repositIerTable:function(table,config){
			let lstp=$(table).data("lstp"),selrow=$.listDnD.filter_rows(table,config,".lstp_"+lstp);
			selrow.each(function(){
				$.listDnD.repositLevel(table,config,this);
			});
		},		
		repositLevel:function(table,config,row){
			let a=$(row).data("id"),selrow=$.listDnD.filter_rows(table,config,".lstp_"+a);
			$(selrow.get().reverse()).each(function(){
				$(this).insertAfter(row);
				$.listDnD.repositLevel(table,config,this);
			});
		},
		renumTable:function(table,config){
			let lstp=$(table).data("lstp"),selrow=$.listDnD.filter_rows(table,config,".lstp_"+lstp),j=0;
			if(config.ierList){
				selrow.each(function(){j++;$.listDnD.renumLevel(table,config,this,j);});
			}else{
				j=$(table).data("sf");
				selrow.each(function(){j++;$(this).find("td.dtnum").text(j);});
			}
		},	
		renumLevel:function(table,config,row,pf){
			let a=$(row).data("id"),j=0,selrow=$.listDnD.filter_rows(table,config,".lstp_"+a);
			$(row).find("td.dtnum").text(pf);
			selrow.each(function(){
				j++;
				$.listDnD.renumLevel(table,config,this,pf+"."+j);
			});
		},					
		createContMenu:function(table){
			let config=table.listDnDConfig,rn=Math.floor(Math.random()*10000),tabId=table.id,hdr;
			if(config.contMenu){$("#contmenu"+config.contMenu).remove();}
			var div=document.createElement('div'),list=document.createElement('ul');
			$(list).addClass('listitems listitems'+rn);
			$.each(config.contMenuList,function(key,value){
				let li='';
				if(!value.tables||value.tables&&value.tables.indexOf(tabId)!=-1){
					if(!$.isEmptyObject(value)){
						let atr=!!value.singl&&!!value.mult?' sel_m sel_s sel_a':!!value.singl?' sel_a sel_s':!!value.mult?' sel_a sel_m':'';
						li='<li class="oneitem '+atr+' aa_'+value.action+'" data-action="'+value.action+'"><a class="" data-action="'+value.action+'">'+((!!value.image)?'<img src="'+config.menuImgPath+'/'+value.image+'" alt="" />':'')+value.label+'</a></li>';
					}else{
						li='<li class="emptyitem"><hr></li>';
					}
					$(list).append(li);
				}
			});
			if(config.multySelect&&!$.isEmptyObject(config.contMenuListSys)){
				$(list).append('<li class="emptyitem"><hr></li>');
				!$.isEmptyObject(config.contMenuListSys.chkall)&&$(list).append('<li class="oneitem sys_sel sel_a sel_nf sys_sel_all"><a>'+((!!config.contMenuListSys.chkall.image)?'<img src="'+config.menuImgPath+'/'+config.contMenuListSys.chkall.image+'" alt="" />':'')+config.contMenuListSys.chkall.label+'</a></li>');
				!$.isEmptyObject(config.contMenuListSys.unchkall)&&$(list).append('<li class="oneitem sys_sel sel_a sel_nn sys_sel_not"><a>'+((!!config.contMenuListSys.unchkall.image)?'<img src="'+config.menuImgPath+'/'+config.contMenuListSys.unchkall.image+'" alt="" />':'')+config.contMenuListSys.unchkall.label+'</a></li>');
				!$.isEmptyObject(config.contMenuListSys.chkrev)&&$(list).append('<li class="oneitem sys_sel sel_a sel_nfn sys_sel_rev"><a>'+((!!config.contMenuListSys.chkrev.image)?'<img src="'+config.menuImgPath+'/'+config.contMenuListSys.chkrev.image+'" alt="" />':'')+config.contMenuListSys.chkrev.label+'</a></li>');
			}
			$(div).append(list);
			if(config.toolBar){
				var pnl=$(div).clone(true).addClass('contpanel');
				if($(table).find('tr.head-cntrl').length==1){
					hdr=$(table).find('tr.head-cntrl')[0];
				}else{
					hdr=document.createElement('tr');
					$(hdr).html('<td colspan="'+this.countCol(table)+'"></td>').addClass("head-cntrl nodrag nodrop");
				}
				$(hdr).find('td:first').prepend(pnl);
				$(pnl).find('a').addClass('side-button');
				$(pnl).on("click.listDnD","a",this.clickInMenu);
				$(table).prepend(hdr);
			}
			$(div).addClass('contmenu').attr('id','contmenu'+rn).appendTo('body');
			$(div).on("mouseenter",".oneitem",function(){$(this).addClass(config.onhoverClass);}).on("mouseleave",".oneitem",function(){$(this).removeClass(config.onhoverClass);});
			config.contMenu=rn;
			this.act_deact_contMenu(table,config);
		},
		act_deact_contMenu:function(table,config){
			let selected=this.filter_rows(table,config,"."+config.onselClass);
			config.countSel=selected.length;
			if(config.contMenu!==0){
				$('.listitems'+config.contMenu+' .sel_a').addClass("notact");
				$('#contmenu'+config.contMenu+' .listitems'+config.contMenu+' .sel_a').not('.sys_sel').each(function(){
					let act=$(this).data('action'),kk=selected.not('.no_'+act).length;
					if(kk){
						config.countSel==1&&$('.listitems'+config.contMenu+' .sel_s.aa_'+act).removeClass("notact");
						kk>1&&$('.listitems'+config.contMenu+' .sel_m.aa_'+act).removeClass("notact");
					}
				});
				if(config.multySelect){
					(config.countSel==0||config.countSel!=config.countList)&&$('.listitems'+config.contMenu+' .sel_nf').removeClass("notact");
					config.countSel>0&&$('.listitems'+config.contMenu+' .sel_nn').removeClass("notact");
					config.countSel>0&&config.countSel!=config.countList&&$('.listitems'+config.contMenu+' .sel_nfn').removeClass("notact");
				}
			}
		},
		contMenuAct:function(e){
			if(!$.listDnD.currentTable){return null;}
			e&&e.preventDefault();e.stopPropagation();
			if($(e.target).closest("."+$.listDnD.currentTable.listDnDConfig.elTrClass).length){
				var table=$.listDnD.currentTable,config=$.listDnD.currentTable.listDnDConfig,menu=document.querySelector("#contmenu"+config.contMenu);
				if(config.countSel==0||!$(e.target).closest("."+$.listDnD.currentTable.listDnDConfig.elTrClass).hasClass(config.onselClass)){
					$.listDnD.dragObject=$(e.target).closest("."+$.listDnD.currentTable.listDnDConfig.elTrClass)[0];
					$.listDnD.processSelect();
				}
				$("#contmenu"+config.contMenu+" .listitems"+config.contMenu).children("li.oneitem").removeClass(config.onhoverClass).not(".notact").filter(":first").addClass(config.onhoverClass);
				$.listDnD.currentContMenu=menu;
				clickedCoor=$.listDnD.mouseCoords(e);
				let scrollBottom=window.innerHeight+window.pageYOffset,rowhei=e.target.clientHeight,xCordinate=clickedCoor.x,yCordinate=clickedCoor.y+rowhei,menuWidth=menu.offsetWidth+10,menuHeight=menu.offsetHeight+5,windowWidth=window.innerWidth,windowHeight=window.innerHeight;
				if((windowWidth-xCordinate)<menuWidth){menu.style.left=(windowWidth-menuWidth)-10+"px";}else{menu.style.left=xCordinate-0+"px";}
				if(yCordinate+menuHeight>scrollBottom+rowhei){menu.style.top=(scrollBottom-menuHeight+rowhei)-0+"px";}else{menu.style.top=(yCordinate-0)+"px";}
				$(menu).show();
			}
		},
		clickInMenu:function(e){
			e&&e.preventDefault();e.stopPropagation();
			if($(e.target).closest($.listDnD.currentContMenu).length||$(e.target).closest('.listitems').length){
				if($(e.target).closest(".oneitem").hasClass("notact")){return null;}
				let actname=$(e.target).closest("a").data("action");
				let table=$.listDnD.currentTable,config=$.listDnD.currentTable.listDnDConfig;
				$.listDnD.runAction(table,config,actname);				
			}
			$.listDnD.closeContMenu();
			return false;
		},		
		closeContMenu:function(){
			if(!this.currentContMenu){return null;}
			$(this.currentContMenu).hide();
			this.currentContMenu=null;
		},		
		currentOrder:function(){
			var rows=this.currentTable.rows;
			return $.map(rows,function(val){return (val.id).replace(/\s/g,'');}).join('');
		},
    	initialiseSelect:function(dragObject,table){
			this.dragObject=dragObject;
			this.currentTable=table;
			$(this.tables).removeClass("focuslist");
			$(table).addClass("focuslist");
			if(!this.keyEventSet){
				$(document).on("click.listDnD",".contmenu a",this.clickInMenu).on("keyup.listDnD",function(e){e.preventDefault();}).on("keydown.listDnD",this.keydownevent);
				this.keyEventSet=true;	
			}
		}, 
		initialiseDrag:function(dragObject,table,target,e,config){
			if(e.button===0){
				this.mouseOffset=this.getMouseOffset(target,e);
				this.originalOrder=this.currentOrder();
				$(table).on(moveEvent,"tr",this.mousemove).on(endEvent,"tr",this.mouseup);
				config.onDragStart&&config.onDragStart(table,target);
			}
		},
		mouseCoords:function(e){
			if(e.originalEvent.changedTouches){return{x:e.originalEvent.changedTouches[0].clientX,y:e.originalEvent.changedTouches[0].clientY};}
			if(e.pageX||e.pageY){return {x:e.pageX,y:e.pageY};}
			return {x:e.clientX+document.body.scrollLeft-document.body.clientLeft,y:e.clientY+document.body.scrollTop-document.body.clientTop};
		},
		getMouseOffset:function(target,e){
			var mousePos,docPos;
			e=e||window.event;
			docPos=this.getPosition(target);
			mousePos=this.mouseCoords(e);
			return {x:mousePos.x-docPos.x,y:mousePos.y-docPos.y};
		},
		getPosition:function(element){
			var left=0,top=0;
			if(element.offsetHeight===0){element=element.firstChild;}
			while(element.offsetParent){
				left+=element.offsetLeft;
				top+=element.offsetTop;
				element=element.offsetParent;
			}
			left+=element.offsetLeft;
			top+=element.offsetTop;
			return {x:left,y:top};
    	},
		autoScroll:function(mousePos,keyb){
      		var config=this.currentTable.listDnDConfig,yOffset=window.pageYOffset,
			windowHeight=window.innerHeight?window.innerHeight:document.documentElement.clientHeight?document.documentElement.clientHeight:document.body.clientHeight;
			if(document.all){
				if(typeof document.compatMode!=='undefined'&&document.compatMode!=='BackCompat'){
					yOffset=document.documentElement.scrollTop;
				}else if(typeof document.body!=='undefined'){
					yOffset=document.body.scrollTop;
				}
			}
			if(!keyb){
				mousePos.y-yOffset<config.scrollAmount&&window.scrollBy(0,-config.scrollAmount)||windowHeight-(mousePos.y-yOffset)<config.scrollAmount&& window.scrollBy(0,config.scrollAmount);
			}else{
				mousePos.y-yOffset<0&&window.scrollBy(0,mousePos.y-yOffset-config.scrollAmount)||windowHeight-(mousePos.y-yOffset)<config.scrollAmount&& window.scrollBy(0,windowHeight-(mousePos.y-yOffset)+(3*config.scrollAmount));				
			}
		},
    	moveVerticle:function(moving,currentRow){
			if(0!==moving&&currentRow&&this.dragObject!==currentRow&&this.dragObject.parentNode===currentRow.parentNode){
				0>moving&&this.dragObject.parentNode.insertBefore(this.dragObject,currentRow.nextSibling)||0<moving&&this.dragObject.parentNode.insertBefore(this.dragObject,currentRow);
			}
    	},
		mousemove:function(e){
        	var dragObj=$($.listDnD.dragObject),config=$.listDnD.currentTable.listDnDConfig,currentRow,mousePos,moving,y;
        	e&&e.preventDefault();
			e.stopPropagation();
        	if(!$.listDnD.dragObject){return false;}
        	e.type==='touchmove'&&event.preventDefault();
        	config.onDragClass&&dragObj.addClass(config.onDragClass);
        	mousePos=$.listDnD.mouseCoords(e);
        	y=mousePos.y-$.listDnD.mouseOffset.y;
        	$.listDnD.autoScroll(mousePos);
        	currentRow=$.listDnD.findDropTargetRow(dragObj,y);
        	moving=$.listDnD.findDragDirection(y);
        	$.listDnD.moveVerticle(moving,currentRow);
			return false;
		},
		findDragDirection:function(y){
			var sensitivity = this.currentTable.listDnDConfig.sensitivity,oldY=this.oldY,yMin=oldY-sensitivity,yMax=oldY+sensitivity,
			moving=y>=yMin&&y<=yMax?0:y>oldY?-1:1;
			if(moving!==0){this.oldY=y;}
			return moving;
		},
		findDropTargetRow:function(draggedRow,y){
			var rowHeight=0,rows=this.currentTable.rows,config=this.currentTable.listDnDConfig,rowY=0,row=null;
			for(var i=0;i<rows.length;i++){
				row=rows[i];
				rowY=this.getPosition(row).y;
				rowHeight=parseInt(row.offsetHeight)/2;
				if(row.offsetHeight===0){
					rowY=this.getPosition(row.firstChild).y;
					rowHeight=parseInt(row.firstChild.offsetHeight)/2;
				}
				if(y>(rowY-rowHeight)&&y<(rowY+rowHeight)){
					if(draggedRow.is(row)||(config.ierList&&$(draggedRow).data("pid")!=$(row).data("pid"))||(config.onAllowDrop&&!config.onAllowDrop(draggedRow,row))||$(row).hasClass("nodrop")){
						return null;
					}else{
						return row;
					}
				}
			}
			return null;
		},
		countCol:function(table){
			let colCount=0;
			$(table).find('tr:nth-child(1) th').each(function(){if($(this).attr('colspan')){colCount+=+$(this).attr('colspan');}else{colCount++;}});
			return colCount;
		},
		get_filtered_rows:function(subj,config,filter=false,range=false){
			var $all_rows,$filtered_rows,$subj=(subj instanceof jQuery)?subj:$(subj);
			if(($subj.length===1)&&($subj.is("table"))){$all_rows=$($subj[0].rows).filter("tr");}else if(($subj.length>0)&&($subj.is("tr"))){$all_rows=$subj.filter("tr");}else{return $();}
			if(range){$all_rows=$all_rows.slice(range[0]+2,range[1]+2);}//плюс два - сдвиг нав строки заголовка надо перенести в настройки
			if(filter){$all_rows=$all_rows.filter(filter);}
			$all_rows=$all_rows.filter("."+config.elTrClass).not(".nosel");
			return $all_rows;
		},
		filter_rows:function(subj,config,filter,range,callback){
			var $filtered_rows;
			$filtered_rows=this.get_filtered_rows(subj,config,filter,range);
			if(typeof callback==='function'){callback($filtered_rows);}
			return $filtered_rows;
		},
		revers_select_rows:function(table,config){
			let old=this.filter_rows(table,config,"."+config.onselClass);
			this.select_all_rows(table,config);
			old.removeClass(config.onselClass);
		},
		unselect_all_rows:function(table,config,range){
			var callback=function($filtered_rows){$filtered_rows.removeClass(config.onselClass);};
			this.filter_rows(table,config,false,range,callback);
		},
		select_all_rows:function(table,config,range){
			var callback=function($filtered_rows){$filtered_rows.addClass(config.onselClass);};
			this.filter_rows(table,config,false,range,callback);
		},
		table_select:function(e){
			if(!$.listDnD.currentTable){return null;}
			if($(e.target).closest(".oneitem").hasClass("notact")){return null;}
			let but=$(e.target).closest(".sys_sel");
			type=but.hasClass("sys_sel_all")?"all":but.hasClass("sys_sel_not")?"not":but.hasClass("sys_sel_rev")?"rev":"";
			var table=$($.listDnD.currentTable),config=$.listDnD.currentTable.listDnDConfig;
			(type==="all"&&$.listDnD.select_all_rows(table,config))||(type==="not"&&$.listDnD.unselect_all_rows(table,config))||(type==="rev"&&$.listDnD.revers_select_rows(table,config));
			$.listDnD.act_deact_contMenu(table,config);
			$.listDnD.closeContMenu();
			let selrow=$.listDnD.filter_rows(table,config,"."+config.onselClass);
			$.listDnD.chkSelect(table[0],selrow);
		},
		processSelect:function(shiftKey,ctrlKey){
			if(!$.listDnD.currentTable||!$.listDnD.dragObject){return null;}
			var table=$(this.currentTable),config=this.currentTable.listDnDConfig,selRow=$(this.dragObject);
			if(!selRow.hasClass("nosel")){
				if(config.multySelect){
					var rowIndex=selRow.index(),prev_rangeEndpoint=config.rangeEndpoint,range;
					config.rangeEndpoint=rowIndex;
					if((!shiftKey)||(typeof config.rangeAnchor!=='number')){config.rangeAnchor=rowIndex;}
					if(shiftKey){
						if(!ctrlKey){
							this.unselect_all_rows(table,config);
						}else{
							range=[Math.min(config.rangeAnchor,prev_rangeEndpoint),Math.max(config.rangeAnchor,prev_rangeEndpoint)+1];
							this.unselect_all_rows(table,config,range);
						}
						range=[Math.min(config.rangeAnchor,config.rangeEndpoint),Math.max(config.rangeAnchor,config.rangeEndpoint)+1];
						this.select_all_rows(table,config,range);
					}else if(ctrlKey){
						selRow.toggleClass(config.onselClass);
						if(!selRow.hasClass(config.onselClass)){
							(function(){
								let $nearest_row,row_index;
								$nearest_row=selRow.prevAll("tr."+config.onselClass+":first");
								if($nearest_row.length!==1){$nearest_row=selRow.nextAll("tr."+config.onselClass+":first");}
								row_index=($nearest_row.length!==1)?false:$nearest_row.index();
								config.rangeAnchor=row_index;
								config.rangeEndpoint=row_index;
							})();
						}
					}else{
						this.unselect_all_rows(table,config);
						selRow.addClass(config.onselClass);
					}	
				}else{
					this.unselect_all_rows(table,config);
					selRow.addClass(config.onselClass);	
				}
			}
			mousePos=$.listDnD.getPosition(this.dragObject);
			$.listDnD.autoScroll(mousePos,true);
			$.listDnD.act_deact_contMenu(table,config);
			let selrow=$.listDnD.filter_rows(table,config,"."+config.onselClass);
			$.listDnD.chkSelect(table[0],selrow);
		},
		processMouseup:function(){
			if(!this.currentTable||!this.dragObject||!this.currentTable.listDnDConfig.isDragable){return null;}
			var config=this.currentTable.listDnDConfig,droppedRow=this.dragObject,chnord=this.originalOrder!==this.currentOrder();
			$(this.currentTable).off(moveEvent).off(endEvent);
			config.onDragClass&&$(droppedRow).removeClass(config.onDragClass);
			config.onDrop&&chnord&&$(droppedRow).hide().fadeIn('fast')&&config.onDrop(this.currentTable,droppedRow);
			chnord&&this.positUpList(this.currentTable,config,droppedRow);
			config.onDragStop&&config.onDragStop(this.currentTable,droppedRow);
		},
		mouseup:function(e){
			e&&e.preventDefault();
			e.stopPropagation();
			$.listDnD.processMouseup();
			return false;
		},
		mouseclick:function(e){
			if($(e.target).closest("."+$.listDnD.currentTable.listDnDConfig.elTrClass).length&&e.button===0){
				$.listDnD.processSelect(e.shiftKey,e.ctrlKey);
				if(!$(e.target).closest('a')){e&&e.preventDefault();e.stopPropagation();}
			}
			$.listDnD.closeContMenu();
		},
		mousedblclick:function(e){
			e&&e.preventDefault();e.stopPropagation();
			if(!$.listDnD.currentTable){return null;}
			let table=$.listDnD.currentTable,config=$.listDnD.currentTable.listDnDConfig;
			!!config.keybList['enter']&&$.listDnD.runAction(table,config,config.keybList['enter']);				
			return false;
		},
		keydownevent:function(e){
			e&&e.preventDefault();e.stopPropagation();
			if(!$.listDnD.currentTable){return null;}
			shiftKey=e.shiftKey,ctrlKey=e.ctrlKey;
			if(!$.listDnD.currentContMenu){$.listDnD.closeContMenu();}
			let table=$.listDnD.currentTable,config=$.listDnD.currentTable.listDnDConfig,row,optList,index;
			if(!$.listDnD.currentContMenu){
				row=$.listDnD.dragObject;optList=$.listDnD.filter_rows(table,config);index=optList.index(row);
			}else{
				optList=$("#contmenu"+config.contMenu+" .listitems"+config.contMenu).children("li.oneitem").not(".notact");
				row=optList.filter("li."+config.onhoverClass)[0];
				if(!row){row=optList.filter(':first')[0];}
				index=optList.index(row);
			}
			let focusOn=function(elm){if(!$.listDnD.currentContMenu){$.listDnD.dragObject=elm[0];$.listDnD.processSelect(shiftKey,ctrlKey);}else{$("#contmenu"+config.contMenu+" .listitems"+config.contMenu).children("li.oneitem").removeClass(config.onhoverClass);$(elm).addClass(config.onhoverClass);}},
			first=function(){focusOn(optList.filter(':first'));},
			last=function() {focusOn(optList.filter(':last'));},
			next=function(i){focusOn(optList.eq(index+i));},
			prev=function(i){focusOn(optList.eq(index-i));},
			callAction=function(key){
				let keyb=(key===13)?"enter":(key===8)?"backspace":(key===32)?"space":(key===45)?"insert":(key===46)?"delete":"";
				!!config.keybList[keyb]&&$.listDnD.runAction(table,config,config.keybList[keyb]);								
			},
			kk=(e.which)?e.which:e.keyCode;
			switch(kk){
				case 13:if(!$.listDnD.currentContMenu){callAction(kk);}else if($(row).hasClass("sys_sel")){$(row).trigger("click.listDnD");$.listDnD.closeContMenu();}break;
				case 27:$.listDnD.closeContMenu();break;
				case 40:(index==optList.length-1||row.length==0)?last():next(1);break;
				case 38:(index==0||row.length==0)?first():prev(1);break;
				case 33:(index-5<=0||row.length==0)?first():prev(5);break;
				case 36:first();break;
				case 34:(index+5>=optList.length-1||row.length==0)?last():next(5);break;
				case 35:last();break;
				case 8:case 32:case 45:case 46:callAction(kk);break;
				default:break;
			}	
			return false;
		},
		/*
		jsonize:function(pretify){
			var table=this.currentTable;
			if(pretify){return JSON.stringify(this.tableData(table),null,table.listDnDConfig.jsonPretifySeparator);}
			return JSON.stringify(this.tableData(table));
		},
		*/
		serialize:function(){return $.param(this.tableData(this.currentTable));},
		/*
    	serializeTable:function(table){
			var result="";
			var paramName=table.listDnDConfig.serializeParamName||table.id;
			var rows=table.rows;
			for(var i=0;i<rows.length;i++){
				var rowId=rows[i].id;
				if(rowId&&table.listDnDConfig&&table.listDnDConfig.serializeRegexp){
					if(result.length>0){result+="&";}
					rowId=rowId.match(table.listDnDConfig.serializeRegexp)[0];
					result+=paramName+"["+rowId+"]=yes";//+rowId;
				}
			}
			return result;
		},
		*/
		tableData:function(table){
			var config=table.listDnDConfig,rowID=null,data={},getSerializeRegexp,paramName,currentID,rows;
			if (!table){table=this.currentTable;}
			if(!table||!table.rows||!table.rows.length){return {error:{code:500,message:"Not a valid table."}};}
			if(!table.id&&!config.serializeParamName){return {error:{code:500,message:"No serializable unique id provided."}};}
			rows=table.rows||$.makeArray(table.rows);
			paramName=config.serializeParamName||table.id;
			currentID=paramName;
			getSerializeRegexp=function(rowId){
				if(rowId&&config&&config.serializeRegexp){return rowId.match(config.serializeRegexp)[0];}
				return rowId;
			};
			data[currentID]=[];
			for(var i=0;i<rows.length;i++){
				rowID=getSerializeRegexp(rows[i].id);
				rowID&&data[currentID].push(rowID);
			}
			return data;
		},
		menuUpdate:function(){
			this.act_deact_contMenu(this.currentTable,this.currentTable.listDnDConfig);
		},
	};
	jQuery.fn.extend({
		listDnD:$.listDnD.build
	});
}(jQuery,window,window.document);
