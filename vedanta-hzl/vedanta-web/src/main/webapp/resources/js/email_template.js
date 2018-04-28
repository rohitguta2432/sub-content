//for Create Email Template information

$(document).ready(function(){

$('#templatebutton').click(function(){
		
			let id=$('#id').val();
			
			  let template = CKEDITOR.instances.editor1.getData();
			  
			  let templateName=$('#templateName').val();
			  let templateTitle=$('#title').val();
			  let templateDescription=$('#description').val();
			  let businessUnitId = $('#businessUnitId').val();
			  
			  var data = {
						'id':id,'name':templateName,
						'title':templateTitle,
						'Description':templateDescription,
						'msgContent':template,
						'businessUnitId' : businessUnitId
				}
			
			if(id==''){	
				
				if(templateName=='' || template=='' || templateTitle==''||templateDescription==''){
					
				}else{
			$.ajax({
					url:'/vedanta-web/email/template/save',
					type: 'POST',
					contentType:'application/json',
				    dataType : 'json',
					data:JSON.stringify(data),
					success:function(response){
						window.location.replace("/vedanta-web/email/admin/template");
					},
					error:function(response){
						window.location.replace("/vedanta-web/email/admin/template");

						}
					});
				}
			}else{
				$.ajax({
					url:'/vedanta-web/email/template/updateById',
					type: 'POST',
					contentType:'application/json',
					dataType : 'json',
					data:JSON.stringify(data),
					success:function(response){
						window.location.replace("/vedanta-web/email/admin/template");
					},
					error:function(response){
						window.location.replace("/vedanta-web/email/admin/template");
					}
			});
		}
	});

});

	