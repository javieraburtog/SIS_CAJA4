/**
 * 
 */


function exportarDocPropuesta( idajuste ){
	
	 $('#wraploader').show();
	 
	  $.ajax({
         type: "POST",
         url: '../SvltExportarPropuestaAjustePDF',
         data : {
             
        	 idajusteexcepcion : idajuste
        	 
         }, success: function(data) {
       	  $('#wraploader').hide();
       	  if(data.trim() == '' ){
               alert('No se encontraron resultados.');
         	  }else{
         		window.open(data,'_blank'); 
         	  }
         }, error: function(data) {
         	 alert('Error al generar el documento.');
             $('#wraploader').hide();
         }
      });
}



function loadSelectAdmin(){

	$( "#cmbOpcionesAdmin" ).show();
	
	loadGrid();
	
}

$('#idSelectOptionAdmin').change(function() {
	
	 $.blockUI({ message: '<h1><img src="../theme/icons/busy.gif" /> Espere un momento...</h1>' });
	
	  switch( parseInt($(this).val()) ) {
	    case 1: loadGrid();  break;
	    case 2: change2();  break;
	    case 3: change3();  break;
	  }
	});


var rows = new Array()  ;
	$(function() {
		$( "#dateInitial" ).datepicker({
			showOn: "button",
			buttonImage: "../resources/images/calendar.gif",
			buttonImageOnly: false,
			dateFormat: 'dd/mm/yy',
			buttonText: "Selecione fecha"
		});
		$( "#dateEnd" ).datepicker({
			showOn: "button",
			buttonImage: "../resources/images/calendar.gif",
			buttonImageOnly: false,
			dateFormat: 'dd/mm/yy',    
			buttonText: "Selecione fecha"
		});	
		
		 $( "#someter" ).button({
			 	
		  });
		
		 // unblock when ajax activity stops 
		    $(document).ajaxStop($.unblockUI); 
		 
		    function test() { 
		        $.ajax({ url: 'wait.php', cache: false }); 
		    } 
		 
	});
	
	
	var cols = null;
	var filters = null;
	
	function loadGrid(){
		    if(rows.length>0){
		    	
				jConfirm('Al cambiar la fuente de la informacion  se borrar los ajustes seleccionados, desea continuar ?', 'Confirmarcion', function(r) {
    				if(eval(r)==true){    					 
    					loadGridContent();
    				}else{
    					if($('input[name="source"]:checked').val()==1){
    						$('input:radio[name=source]').val(['2']);      					
    					}else{
    						$('input:radio[name=source]').val(['1']);
    					}
    					
    				}  
				});
		    }else{
				loadGridContent();
				 
				$('#propuestaAjuste').fadeIn("slow"); 
				
				$("#RegistroAjuste").hide();
				$("#procesoAjusteA").hide();
				
//				$("#TablaprocesoAjusteA").html("");
//				$("#TablaRegistroAjuste").html("");
				
		    }
	}
	
	
	function loadGridContent(){
		rows = new Array();		
		 $("#TablaprocesoAjuste").html("");
	 	 $('#procesoAjuste').fadeOut( "slow");
		if($('input[name="source"]:checked').val()==1){
				cols = [
			                {display: 'Seleccionar', name : 'Seleccionar', width : 55, sortable : false, align: 'center'},
			                {display: 'Cuenta', name : 'Cuenta', width :70, sortable : true, align: 'right',nowrap : true}, 
			                {display: 'Referencia', name : 'Referencia', width : 75, sortable : true, align: 'right',nowrap : false},	
			                {display: 'Monto', name : 'Monto', width : 80, sortable : true, align: 'right',nowrap : true},
			                {display: 'Banco', name : 'Banco', width : 130, sortable : true, align: 'center',nowrap : true},
			                {display: 'Moneda', name : 'Moneda', width : 45, sortable : true, align: 'center',nowrap : true},
			                {display: 'Fecha', name : 'fecha', width : 65, sortable : true, align: 'center',nowrap : true},
			                {display: 'Tipo', name : 'Tipo', width : 45, sortable : false, align: 'center',nowrap : true}
			            ];
			    filters =   [
				                {display: 'Cuenta', name : 'Cuenta'},
				                {display: 'Referencia', name : 'Referencia'},
				                {display: 'Banco', name : 'Banco'},
				                {display: 'Moneda', name : 'Moneda'}
			                ];    
			     createGrid();           
			}else{  
				cols = [
				                {display: 'Seleccionar', name : 'Seleccionar', width : 55, sortable : false, align: 'center'},
				                {display: 'Cuenta', name : 'Cuenta', width :53, sortable : true, align: 'right',nowrap : true}, 
				                {display: 'Referencia', name : 'Referencia', width : 60, sortable : true, align: 'right',nowrap : false},	
				                {display: 'Monto', name : 'Monto', width : 70, sortable : true, align: 'right',nowrap : true},
				                {display: 'Banco', name : 'Banco', width : 120, sortable : true, align: 'center',nowrap : true},
				                {display: 'Moneda', name : 'Moneda', width : 45, sortable : true, align: 'center',nowrap : true},
				                {display: 'Caja', name : 'caja', width : 140, sortable : true, align: 'center',nowrap : true},
				                {display: 'Fecha', name : 'fecha', width : 55, sortable : true, align: 'center',nowrap : true},
				                {display: 'Tipo', name : 'Tipo', width : 45, sortable : false, align: 'center',nowrap : true}
				       ];
		        filters =   [  
		                {display: 'Cuenta', name : 'Cuenta'},
		                {display: 'Referencia', name : 'Referencia'},
		                {display: 'Banco', name : 'Banco'},
		                {display: 'Moneda', name : 'Moneda'},
		                {display: 'Caja', name : 'caja'}
		               ];   
		          createGrid();     
				}
			   
	}
	

	
	function createGrid(){
		$("#tbContent").html( "<table width=\"100%\" id=\"TblDatos\" style=\"display: none;\"></table>");
		 
		$("#TblDatos").flexigrid({
			url:'../svltECMI',
	        dataType: 'json',
	        colModel : cols,	        
	        searchitems :filters ,  
	        sortname: "",
	        sortorder: "asc",
	        usepager: true,
	        title: 'Depositos',
	        useRp: true,
	        rp: 50,
	        showTableToggleBtn: false,
	        singleSelect: true,
	        height: 400,
	        width: 900,
	        resizable: false
	    });	
	}
	
	function setCurrentDate(){
		    $('#dateEnd').datepicker().datepicker('setDate', 'today');	
	}
	
	function loadInformation(id,object,idobject){
		//alert($('#'+id).is(":checked"));  
		if(eval($('#'+id).is(":checked"))== true){  
		//	alert('si');  
			rows[rows.length] = idobject +"->"+object;
			//alert(rows.toString());
		}else{
			for(var i=0; i<rows.length; i++){
				if(rows[i].split("->")[0]==idobject){
					//	alert('no');      
					rows.splice(i,1);  
				}
			} 
		}
		//alert("contenido array: "+rows.toString());
		
	}
	
	function confirmar(id,row,band){
		if(eval(id)=='5' || eval(id)=='6'){
			 $( "#dialogComment" ).dialog({
				 resizable: false,
				 height:220,
				 modal: true
			 });
			 $( "#trow" ).text(row+"-"+id);
		}else{    
		
			if(rows.length>0 || eval(band)){
				
				jConfirm('Esta seguro que desea procesar estos registros ?', 'Confirmacion', function(r) {
		    		if(eval(r)==true){
		    			
		    			procesar(id,row);
		    		}
		    		
				});
			}else{
				jAlert("Debe Seleccionar registros",'Atencion');
			}
		}
	}   
	
	function procesar(id,row){
	    $.blockUI({ message: '<h1><img src="../theme/icons/busy.gif" /> Espere un momento...</h1>' }); 
		$.ajax({
	   	type: 'POST',
	   	url:  '../svltPA',  
	   	data: {
	   		chks : rows.toString(),
	   		mode: id,
	   		rowid: row,
	   		idObject: $("#Observacion").val()
	   	},
	   	success: function(data){
	   		var obj = jQuery.parseJSON( data );	 
	   		
	   		if(obj.state !=-1){
	   			
	 		if(eval(id)==1){  
	 			rows = new Array();
	 			loadGrid();	
	 			if(obj.state !=0){
	 				jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');
	 			}else{
	 				jAlert(obj.message, 'Atencion');
	 			}
	 		}else if(eval(id)==2 || eval(id)==3 ){	
	 			if(obj.state !=0){
	 				rows = new Array();	
	 				createGridConfirmacionAjuste();	 	
	 				jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');	 							
	 			}else{
	 				jAlert(obj.message, 'Atencion');
	 			}
	 		}else if(eval(id)==4){	
	 			if(obj.state !=0){
	 				rows = new Array();	
	 				createGridConfirmacionAjusteA();	 	
	 				jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');	   							
	 			}else{
	 				jAlert(obj.message, 'Atencion');
	 			}
	 		}else if(eval(id)==5){	
	 			rows = new Array();	
	 			closePopUp();
	 			createGridConfirmacionAjusteA();	 			
	 			jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');
	 		}else if(eval(id)==6){	
	 			rows = new Array();	
	 			closePopUp();
	 			createGridAprobacionAjusteA();	 			
	 			jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');
	 		}else if(eval(id)==7){		 			
	 			if(obj.state !=0){
	 				rows = new Array();	
		 			createGridAprobacionAjusteA();	 			
		 			jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');   							
	 			}else{
	 				jAlert(obj.message, 'Atencion'); 
	 				createGridAprobacionAjusteA();	 
	 			}
	 			
	 		}else if(eval(id)==8){		 			
	 			if(obj.state !=0){
	 				rows = new Array();	
	 				createGridAjusteAprobados();	 			
		 			jAlert("Registros Procesados: "+obj.totalRegs, 'Atencion');   							
	 			}else{
	 				jAlert(obj.message, 'Atencion'); 
	 				createGridAjusteAprobados();	 
	 			}
	 			
	 		}  
	 		
	   		}else{
	   			jAlert(obj.message, 'Atencion'); 
	   			location.reload(true);
	   		}
	    }
	 });
	}  
	
	function saveAdj(row){
		var ids = row.split("@");
		var nvalue ;
		
		
		if(eval($('input[name="ajr_'+ids[2]+'"]:checked').val())==eval('1')){
			 nvalue = "true";
		}else{
			$("#aji_"+ids[2]).removeAttr('disabled');
			nvalue = "false";  
		}
		var band = true;
		for(var i=0; i<rows.length; i++){
			//alert('comparacion: '+rows[i].split("@")[0]+rows[i].split("@")[1]+rows[i].split("@")[2]+" --> "+ids[0]+ids[1]+ids[2]); 
			if(rows[i].split("@")[0]+rows[i].split("@")[1]==ids[0]+ids[1]){
				//	alert('no');       
				rows[i] = ids[0]+"@"+ids[1]+"@"+nvalue+"@"+ids[3];
				band = false;
			}
		}   
		if(eval(band)){
			 //rows[0] = 1; 
			rows[rows.length] =  ids[0]+"@"+ids[1]+"@"+nvalue+"@"+ids[3];  
		}
		
	}
	
	function saveComment(row){		
		//alert('size: '+rows.length + " row " + rows);  
		
		for(var i=0; i<rows.length; i++){
			var rowA = row.split("@");
			var rowsAdjA =  rows[i].split("@");
			//alert('comparacion: '+rowA[0]+" -- "+rowA[1]+" --> "+rowsAdjA[0]+" -- "+rowsAdjA[1]);
			if(rowA[0]+rowA[1]==rowsAdjA[0]+rowsAdjA[1]){
				 
				//alert( $("#aji_"+rowA[2]).val() ) ;
				//alert( $("#aji_"+rowA[2]).val().replace(/,/g , " ") ) ;
				
				rows[i] = 
					 rows[i].split("@")[0]+"@"
					+rows[i].split("@")[1]+"@"
					+rows[i].split("@")[2]+"@"
					+$("#aji_"+rowA[2]).val().replace(/,/g , " ")+"@"
					+rows[i].split("@")[4];	
			}
		}		  
	}
	
	
	function SaveAdjC(id,row){
		//alert('SaveAdjC: '+id+" " +row.split("@"));  
		var ids = row.split("@");
		if(eval($('#'+id).is(":checked"))== true){  
//				alert(ids[2]);  
				
				$("#aji_"+ids[2]).removeAttr('disabled');
				$("#ajs_"+ids[2]).removeAttr('disabled');
				rows[rows.length] = row;
				
//				alert(rows.toString());
				
			}else{
				
				$("#aji_"+ids[2]).attr('disabled','disabled');
				$("#ajs_"+ids[2]).attr('disabled', 'disabled');
				
				for(var i=0; i<rows.length; i++){
//					alert('comparacion: '+rows[i].split("@")[0]+rows[i].split("@")[1]+rows[i].split("@")[2]+" --> "+ids[0]+ids[1]+ids[2]); 
					if(rows[i].split("@")[0]+rows[i].split("@")[1]+rows[i].split("@")[2]==ids[0]+ids[1]+ids[2]){
						//	alert('no');      
						rows.splice(i,1);  
					}
				} 
			}		
	}
	
	function saveAccountValue(row){
		
		for(var i=0; i<rows.length; i++){
			var rowA = row.split("@");
			var rowsAdjA =  rows[i].split("@");
		//	alert('comparacion: '+rowA[0]+" -- "+rowA[1]+" --> "+rowsAdjA[0]+" -- "+rowsAdjA[1]);
			if(rowA[0]+rowA[1]==rowsAdjA[0]+rowsAdjA[1]){
				//alert('si');
				rows[i] = rows[i].split("@")[0]+"@"+rows[i].split("@")[1]+"@"+rows[i].split("@")[2]+"@"+rows[i].split("@")[3]+"@"+$("#ajs_"+rowA[2]).val();	
				//alert('val: '+rows.toString());
			}
		}	
	}
	
	/////////////////
	
	
	
	function change(){
		
		if(rows.length>0){  
			jConfirm('Esta seguro que desea continuar, se elminara la informacion seleccionada?', 'Confirmacion', function(r) {
				if(eval(r)==true){
					 $('#propuestaAjuste').fadeOut( "fast");
				 	 $("#tbContent").html("");
				 	 $('#procesoAjuste').fadeIn( "slow");
				 	  rows = new Array();		
				 	 createGridConfirmacionAjuste();
    			}
    		
			});
		
		}else{ 
			
			 $('#propuestaAjuste').fadeOut( "slow");
		 	 $("#tbContent").html("");
		 	 $('#procesoAjuste').fadeIn( "slow");
		 	  rows = new Array();		
		 	 createGridConfirmacionAjuste();
		}
		
		
		  }
	  
	 //change();
	 function createGridConfirmacionAjuste(){
		$("#TablaprocesoAjuste").html( "<table width=\"100%\" id=\"TblDatosC\" style=\"display: none;\"></table>");
		
		$("#TblDatosC").flexigrid({
	        url:'../svltCA',
	        dataType: 'json',
	        colModel : [
		                {display: ' ', name : 'Eliminar', width : 35, sortable : false, align: 'center'},
		                {display: 'Seleccionar', name : 'Seleccionar', width :60, sortable : false, align: 'center',nowrap : true}, 
		                {display: 'Cuenta', name : 'Cuenta', width : 60, sortable : true, align: 'center',nowrap : false},
		                {display: 'Partida', name : 'cuenta_o', width : 75, sortable : true, align: 'center',nowrap : false},
		                {display: 'Contra-Partida', name : 'cuenta_g', width : 120, sortable : false, align: 'center', nowrap : true},
		                {display: 'Comentario', name : 'Comentario', width : 200, sortable : false, align: 'left',nowrap : true} ,
		                {display: 'Referencia', name : 'Referencia', width : 60, sortable : true, align: 'right',nowrap : false},	
		                {display: 'Monto', name : 'Monto', width : 60, sortable : true, align: 'right',nowrap : true},
		                {display: 'Moneda', name : 'Moneda', width : 45, sortable : true, align: 'center',nowrap : true},
		                {display: 'Fecha', name : 'Fecha', width : 55, sortable : false, align: 'center',nowrap : true},
		                {display: 'Origen', name : 'Origen', width : 80, sortable : true, align: 'center',nowrap : true},
		                {display: 'Usuario', name : 'Usuario', width : 80, sortable : false, align: 'center', nowrap : true}
			   ],	        
	        searchitems : [    
		                {display: 'Cuenta', name : 'Cuenta'},
		                {display: 'Referencia', name : 'Referencia'},
		                {display: 'Banco', name : 'Banco'},
		                {display: 'Moneda', name : 'Moneda'},
		                {display: 'Caja', name : 'caja'}
		                 ] ,  
	        sortname: "",
	        sortorder: "asc",
	        usepager: true,
	        title: 'Depositos',
	        idObject: "1",
	        useRp: true,
	        rp: 50,
	        showTableToggleBtn: false,
	        singleSelect: true,
	        height: 300,
	        width: 900,
	        resizable: false
	    });	
		
	}
	
	
	function closePopUp(){
		 $( "#dialogComment" ).dialog("close");  
		 $( "#Observacion" ).val("");  
	}
	
	function procesarCancelacion(){
		jConfirm('Esta seguro que desea procesar estos registros?', 'Confirmatcion', function(r) {
    		if(eval(r)==true){
    			procesar($("#trow").text().split("-")[1], $("#trow").text().split("-")[0]);
    		}
    		
		});
	}
	
	//codigo de venta de aprobacion 
	
	
	 function createGridConfirmacionAjusteA(){
			$("#TablaprocesoAjusteA").html( "<table width=\"100%\" id=\"TblDatosCA\" style=\"display: none;\"></table>");
			
			$("#TblDatosCA").flexigrid({
		        url:'../svltCA',
		        dataType: 'json',
		        colModel : [  
		                {display: ' ', name : 'Eliminar', width : 35, sortable : false, align: 'center'},
		                {display: 'Seleccionar', name : 'Seleccionar', width :60, sortable : false, align: 'center',nowrap : true}, 
		                {display: 'Cuenta', name : 'Cuenta', width : 60, sortable : true, align: 'center',nowrap : false},
		                {display: 'Partida', name : 'cuenta_o', width : 75, sortable : true, align: 'center',nowrap : false},
		                {display: 'Contra-Partida', name : 'cuenta_g', width : 90, sortable : true, align: 'center', nowrap : true},
		                {display: 'Comentario', name : 'Comentario', width : 200, sortable : false, align: 'left',nowrap : true} ,
		                {display: 'Referencia', name : 'Referencia', width : 60, sortable : true, align: 'right',nowrap : false},	
		                {display: 'Monto', name : 'Monto', width : 60, sortable : true, align: 'right',nowrap : true},
		                {display: 'Moneda', name : 'Moneda', width : 45, sortable : true, align: 'center',nowrap : true},
		                {display: 'Fecha', name : 'Fecha', width : 55, sortable : false, align: 'center',nowrap : true},
		                {display: 'Origen', name : 'Origen', width : 80, sortable : true, align: 'center',nowrap : true},
		                {display: 'Usuario', name : 'Usuario', width : 80, sortable : false, align: 'center',nowrap : true}
				   ],	        
		        searchitems : [    
			                {display: 'Cuenta', name : 'Cuenta'},
			                {display: 'Referencia', name : 'Referencia'},
			                {display: 'Banco', name : 'Banco'},
			                {display: 'Moneda', name : 'Moneda'},
			                {display: 'Caja', name : 'caja'},
			                {display: 'Usuario', name : 'Usuario'}
			                 ] ,  
		        sortname: "",
		        sortorder: "asc",
		        usepager: true,
		        title: 'Depositos',
		        idObject: "2",
		        useRp: true,
		        rp: 50,
		        showTableToggleBtn: false,
		        singleSelect: true,
		        height: 300,
		        width: 900,
		        resizable: false
		    });	
			
		}
		
		function change2(){
			
			
			$("#procesoAjusteA").show();
			
		 	$('#propuestaAjuste').fadeOut( "slow");
		 	$('#procesoAjuste').fadeOut( "slow");
		 	$('#propuestaAjusteAC').fadeOut( "slow");
		 	$("#tbContent").html("");
		 	$("#TablaprocesoAjuste").html("");
		 	$('#procesoAjusteA').fadeIn( "slow");
		 	 
		 	$("#TablaRegistroAjuste").html("");
		 	 
		 	$("#RegistroAjuste").hide();
		 	
		 	 rows = new Array();		
		 	 createGridConfirmacionAjusteA();
		 	 
		 } 
	  	// change2();
	  	 
	  	 
	  	 //fin de ventanas de aprobacion
	
		
		
		function saveCommentF(row){		
//			alert('size: '+rows.length);  
			for(var i=0; i<rows.length; i++){
				var rowA = row.split("@");
				var rowsAdjA =  rows[i].split("@");
//				alert('comparacion: '+rowA[0]+" -- "+rowA[1]+" --> "+rowsAdjA[0]+" -- "+rowsAdjA[1] +" valor row2: "+rowA[2]);
				if(rowA[0]+rowA[1]==rowsAdjA[0]+rowsAdjA[1]){
//					alert('si');  
					rows[i] = rows[i].split("@")[0]+"@"+rows[i].split("@")[1]+"@"+rows[i].split("@")[2]+"@"+$("#aji_"+rowA[2]).val();	
//					alert('val: '+rows.toString());
				}
			}		  
		}
		
		// carga de seccion de aprobacion final
		
		function createGridAprobacionAjusteA(){
			$("#TablaRegistroAjuste").html( "<table width=\"100%\" id=\"TblDatosAA\" style=\"display: none;\"></table>");
			
			$("#TblDatosAA").flexigrid({
		        url:'../svltRA',
		        dataType: 'json',
		        colModel : [
		                    /*1*/ {display: ' ', name : 'Seleccionar', width :80, sortable : false, align: 'center',nowrap : true}, 
		                    /*2*/ {display: 'Accion', name : 'Accion', width : 180, sortable : false, align: 'center',nowrap : false},
		                    /*3*/ {display: 'Fecha', name : 'fecha', width : 65, sortable : true, align: 'center',nowrap : true},
		                    /*4*/ {display: 'Usuario', name : 'Usuario', width : 80, sortable : false, align: 'center',nowrap : true},
		                    /*5*/ {display: 'Monto', name : 'Monto', width : 70, sortable : true, align: 'right',nowrap : true},
			                /*6*/ {display: 'Moneda', name : 'Moneda', width : 50, sortable : true, align: 'center',nowrap : true},
			                /*7*/ {display: 'Documentos', name : 'Documentos', width : 60, sortable : false, align: 'center', nowrap : true},
		                    /*8*/ {display: 'Compania', name : 'Compania', width :140, sortable : false, align: 'left',nowrap : true} 
		                    ],
		                    
		        searchitems : [    
			                {display: 'Cuenta', name : 'Cuenta'},
			                {display: 'Referencia', name : 'Referencia'},
			                {display: 'Banco', name : 'Banco'},
			                {display: 'Moneda', name : 'Moneda'},
			                {display: 'Caja', name : 'caja'},
			                {display: 'Usuario', name : 'Usuario'}
			                 ] ,  
		        sortname: "",
		        sortorder: "asc",
		        usepager: true,
		        title: 'Depositos',
		        idObject: "2",
		        useRp: true,
		        rp: 50,
		        showTableToggleBtn: false,
		        singleSelect: true,
		        height: 300,
		        width: 900,
		        resizable: false
		    });	
			
		}
		
		function change3(){
		 	 $('#propuestaAjuste').fadeOut( "slow");
		 	 $('#procesoAjuste').fadeOut( "slow");
		 	 $('#propuestaAjusteAC').fadeOut( "slow");
		 	 $('#procesoAjusteA').fadeOut( "slow");
		 	 $('#AjustesAprobados').fadeOut( "slow");
		 	 $("#tbContent").html("");
		 	 $("#TablaprocesoAjuste").html("");
		 	  $("#TablaprocesoAjusteA").html("");
		 	 $("#TablaAjustesAprobados").html("");
		 	 
		 	 $('#RegistroAjuste').fadeIn( "slow");
		 	  rows = new Array();		
		 	 createGridAprobacionAjusteA();
		 } 
	
			// carga de seccion de aprobacion final
			function createGridAjusteAprobados(){
				$("#TablaAjustesAprobados").html( "<table width=\"100%\" id=\"TblDatosAAprob\" style=\"display: none;\"></table>");
				
				$("#TblDatosAAprob").flexigrid({
			        url:'../svltRLA',
			        dataType: 'json',
			        colModel : [
			                {display: ' ', name : 'Eliminar_Ajuste', width :80, sortable : false, align: 'center',nowrap : true}, 
			                {display: 'Cuenta', name : 'cuenta', width : 60, sortable : true, align: 'right',nowrap : false},
			                {display: 'Referencia', name : 'Referencia', width : 60, sortable : true, align: 'right',nowrap : false},	
			                {display: 'Monto', name : 'Monto', width : 60, sortable : true, align: 'right',nowrap : true},
			                {display: 'Caja', name : 'Caja', width : 140, sortable : true, align: 'left',nowrap : true},
			                {display: 'Origen', name : 'Origen', width : 35, sortable : true, align: 'left',nowrap : true},
			                {display: 'Banco', name : 'Banco', width : 120, sortable : true, align: 'center',nowrap : true},
			                {display: 'Moneda', name : 'Moneda', width : 45, sortable : true, align: 'center',nowrap : true},
			                {display: 'Fecha', name : 'fecha', width : 55, sortable : true, align: 'center',nowrap : true},
			                {display: 'Tipo', name : 'Tipo', width : 45, sortable : false, align: 'left',nowrap : true},
			                {display: 'Cuenta Ajuste', name : 'Ajuste', width : 130, sortable : false, align: 'center',nowrap : true},
			                {display: 'Comentario', name : 'Comentario', width : 200, sortable : false, align: 'center',nowrap : true},
			                {display: 'Usuario', name : 'Usuario', width : 80, sortable : false, align: 'center',nowrap : true}
					   ],	        
			        searchitems : [    
				                {display: 'Cuenta', name : 'Cuenta'},
				                {display: 'Referencia', name : 'Referencia'},
				                {display: 'Banco', name : 'Banco'},
				                {display: 'Moneda', name : 'Moneda'},
				                {display: 'Caja', name : 'caja'},
				                {display: 'Usuario', name : 'Usuario'}
				                 ] ,  
			        sortname: "",
			        sortorder: "asc",
			        usepager: true,
			        title: 'Depositos',
			        idObject: "2",
			        useRp: true,
			        rp: 50,
			        showTableToggleBtn: false,
			        singleSelect: true,
			        height: 400,
			        width: 980,
			        resizable: false
			    });	
				
			}
			
			//GRID QUE MANEJA LOS REGISTROS PROCESADOS
			function changeToApproved(){
				 $('#propuestaAjuste').fadeOut( "slow");
			 	 $('#procesoAjuste').fadeOut( "slow");
			 	 $('#propuestaAjusteAC').fadeOut( "slow");
			 	 $('#procesoAjusteA').fadeOut( "slow");
				 $('#RegistroAjuste').fadeOut( "slow");
			 	 
			 	 $("#tbContent").html("");
			 	 $("#TablaprocesoAjuste").html("");
			 	 $("#TablaprocesoAjusteA").html("");
			 	 
			 	 $("#TablaRegistroAjuste").html("");
			 	 
			 	 $('#AjustesAprobados').fadeIn( "slow");
			 	  rows = new Array();		
			 	 createGridAjusteAprobados();
			
			  }
			