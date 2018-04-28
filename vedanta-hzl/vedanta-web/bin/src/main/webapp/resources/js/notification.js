$(document).ready(function(){
	notifyMe();
});

var obj = "";

function notifyMe() {

	$
			.ajax({
				url : "/vedanta-web/notification/getNotificationById",
				type : "GET",
				success : function(data) {
                 //  console.log(data);
					obj = "";
					obj = data;
					var msg = "";
					var count = 0;
					var totalCount = 0;

					var htm = "";
					var flag = "";
					var templateId = 0; 
					var scorecardId = 0;
					var groupId = 0;
					var tempTemplate=0;
					var tempUserId=0;
					var tempFlag=0;
					if(data.length <= 0 ){
						 var caption ="<li> No New Notification </li>";
						 $('#ulNotify').addClass("mylist").append(caption);
					}
					
				
					for (var i = 0; i < data.length; i++) {
	 
						if (data[i].status == 0) {
							// new assigned					    															
								var assignDate = new Date(data[i].assignedDate);


								var d = "<span> Assigned Date:" + assignDate.getFullYear()


										+ "-" + (assignDate.getMonth() + 1) + "-"
										+ assignDate.getDate() + " "
										+ assignDate.getHours() + ":"
										+ (assignDate.getMinutes()<10?'0':'') + assignDate.getMinutes()+"</span>";

								
				
								if (data[i].checked == 0) {


								if(data[i].groupName !== undefined){
			
									msg = "New scorecard <strong>"+data[i].templateName+"</strong>,having group <strong>"+data[i].groupName+"</strong> contract no "
									+ data[i].contractNo
									+ " is assigned to you <br>" + d;
									
									var href = "/vedanta-web/scorecard/user/"
										+ data[i].scorecardId;


								htm = "<li> <a  href='"
										+ href + "'>" + msg + "</a> </li> <br>";


								var $ul = $('#ulNotify').addClass("mylist").append(
										htm);
								count = count + 1;
								totalCount =totalCount + 1;
								templateId = data[i].templateId;
								scorecardId = data[i].scorecardId;
									
								}else{
								
									if(templateId !==data[i].templateId || scorecardId !== data[i].scorecardId){
										msg = "New scorecard <strong>"+data[i].templateName+"</strong>, contract no "
										+ data[i].contractNo
										+ " is assigned to you <br>" + d;
										
										var href = "/vedanta-web/scorecard/user/"
											+ data[i].scorecardId;


										htm = "<li> <a  href='"
											+ href + "'>" + msg + "</a> </li> <br>";


									var $ul = $('#ulNotify').addClass("mylist").append(
											htm);
									count = count + 1;
									totalCount =totalCount + 1;
									templateId = data[i].templateId;
									scorecardId = data[i].scorecardId;
									}	
							
								}
							}else{
								
								if(data[i].groupName !== undefined){
								
							
								msg = "New scorecard <strong>"+data[i].templateName+"</strong>,having group <strong>"+data[i].groupName+"</strong>, contract no "
								+ data[i].contractNo
								+ " is assigned to you <br>" + d;
								
								var href = "/vedanta-web/scorecard/user/"
									+ data[i].scorecardId;


								htm = "<li> <a  href='"
									+ href + "'>" + msg + "</a> <li> <br>";
								var $ul = $('#ulNotify').addClass("mylist").append(
									htm);
								//new add 
								totalCount =totalCount + 1;

								
								}else{
									msg = "New scorecard <strong>"+data[i].templateName+"</strong>, contract no "
									+ data[i].contractNo
									+ " is assigned to you <br>" + d;
									


									var href = "/vedanta-web/scorecard/user/"
										+ data[i].scorecardId;



									htm = "<li> <a  href='"
										+ href + "'>" + msg + "</a> <li> <br>";
									var $ul = $('#ulNotify').addClass("mylist").append(
										htm);
									totalCount =totalCount + 1;
								}	
							}	
	
						
						}  else if (data[i].status == 1) {
							// new submmitted

							var poItem = data[i].poItem;
							if (data[i].checked == 0) {
							if(groupId !== data[i].groupId){
								var submittedOn = new Date(data[i].submittedOn);
                                 
                                    
								var d = "<span> Submitted Date:"


										+ submittedOn.getFullYear() + "-"
										+ (submittedOn.getMonth() + 1) + "-"
										+ submittedOn.getDate() + " "
										+ submittedOn.getHours() + ":"
										+ (submittedOn.getMinutes()<10?'0':'') + submittedOn.getMinutes()+"</span>";
							// common group checking 
								
								
								if(tempTemplate !== data[i].templateId || tempUserId !== data[i].userId){
									
									
									 var groupCount=0;
									tempTemplate = data[i].templateId 
                                    for(var k=0; k < data.length; k++){
                                   	 // how many group in single template
                                   	 if(tempTemplate  == data[k].templateId){
                             
                                   		 groupCount = groupCount + 1;
                                   		// console.log(groupCount);
                                   	 }
                                   	 
                                    }
                               
									if(groupCount > 1){
										msg = "Scorecard <strong>"+data[i].templateName+"</strong> submitted by : <strong>"
										+ data[i].userName + "</strong> "
										+ ", contract no  "
										+ data[i].contractNo
										+ " and PO item No -" + data[i].poItem
										+ "<br>" + d;
								
									}else{
										msg = "Scorecard <strong>"+data[i].templateName+"</strong> having group <strong>"+data[i].groupName+"</strong> submitted by : <strong>"
										+ data[i].userName + "</strong> "
										+ ", contract no  "
										+ data[i].contractNo
										+ " and PO item No -" + data[i].poItem
										+ "<br>" + d;
							
									}
									
	
									
									var href = "/vedanta-web/scorecard/user/"
									+ data[i].scorecardId;
									htm = "<li> <a  href='"
									+ href + "'>" + msg + "</a> <li>";
									var $ul = $('#ulNotify').addClass("mylist")
									.append(htm);
									count = count + 1;
									groupId = data[i].groupId
									flag = data[i].poItem;
									tempTemplate = data[i].templateId;
									tempUserId = data[i].userId;
									totalCount =totalCount + 1;
								}
								

							
							}
						}else{
							var submittedOn = new Date(data[i].submittedOn);
							var d = "<span> Submitted Date:"
									+ submittedOn.getFullYear() + "-"
									+ (submittedOn.getMonth() + 1) + "-"
									+ submittedOn.getDate() + " "
									+ submittedOn.getHours() + ":"
									+ (submittedOn.getMinutes()<10?'0':'') + submittedOn.getMinutes()+"</span>";

							    var groupCount=0;
								if(tempTemplate !== data[i].templateId || tempUserId !== data[i].userId){
								
									tempTemplate = data[i].templateId 
                                     for(var k=0; k < data.length; k++){
                                    	 // how many group in single template
                                    	 if(tempTemplate  == data[k].templateId){
                                    	
                                    		 groupCount = groupCount + 1;
                                    		 console.log(groupCount);
                                    	 }
                                    	 
                                     }
                                
									if(groupCount > 1){
										msg = "Scorecard <strong>"+data[i].templateName+"</strong> submitted by :<strong> "
										+ data[i].userName + "</strong> "
										+ ", contract no  "
										+ data[i].contractNo
										+ " and PO item No -" + data[i].poItem
										+ "<br>" + d;
								
									}else{
										msg = "Scorecard <strong>"+data[i].templateName+"</strong> having group "+data[i].groupName+" submitted by : <strong>"
										+ data[i].userName + "</strong> "
										+ ", contract no  "
										+ data[i].contractNo
										+ " and PO item No -" + data[i].poItem
										+ "<br>" + d;
							
									}
									
						
									var href = "/vedanta-web/scorecard/user/"
									+ data[i].scorecardId;
									htm = "<li> <a  href='"
									+ href + "'>" + msg + "</a> <li>";
									var $ul = $('#ulNotify').addClass("mylist")
									.append(htm);
									groupId = data[i].groupId
									flag = data[i].poItem;
									tempTemplate = data[i].templateId;
									tempUserId = data[i].userId;
									totalCount =totalCount + 1;
								}

							
						}


						}  else if (data[i].status == 2) {
							
							var poItem = data[i].poItem;
							if(templateId !== data[i].templateId || scorecardId !== data[i].scorecardId){
							
							if (data[i].checked == 0) {
								msg = "Scorecard <strong>"+data[i].templateName+"</strong> approved successfully. Approved by:<strong>"
										+ data[i].approvedBy + "</strong>, contract no  "
										+ data[i].contractNo;
								var href = "/vedanta-web/scorecard/user/"
										+ data[i].scorecardId;
								


								htm = "<li> <a   href='"
										+ href + "'>" + msg + "</a> <li>";


								var $ul = $('#ulNotify').addClass("mylist")
										.append(htm);
								count = count + 1;
								totalCount =totalCount + 1;
							
							}else{
								msg = "Scorecard <strong>"+data[i].templateName+"</strong> approved successfully. Approved by:<strong>"
								+ data[i].approvedBy + "</strong>, contract no  "
								+ data[i].contractNo;
								var href = "/vedanta-web/scorecard/user/"
								+ data[i].scorecardId;
						
								htm = "<li> <a   href='"
								+ href + "'>" + msg + "</a> <li>";
								var $ul = $('#ulNotify').addClass("mylist")
								.append(htm);
								totalCount =totalCount + 1;
								
							}
						
							templateId = data[i].templateId;
							scorecardId = data[i].scorecardId;
							}	

						} 
						else if (data[i].status == 3) {
							
							var poItem = data[i].poItem;
							if(templateId !== data[i].templateId || scorecardId !== data[i].scorecardId){
							
							if (data[i].checked == 0) {
								msg = "Scorecard <strong>"+data[i].templateName+"</strong> rejected, Please fill again. Rejected by:<strong>" + data[i].approvedBy + "</strong>, Contract no "
										+ data[i].contractNo;
								var href = "/vedanta-web/scorecard/user/"
										+ data[i].scorecardId;
								


								htm = "<li> <a   href='"
										+ href + "'>" + msg + "</a> <li>";


								var $ul = $('#ulNotify').addClass("mylist")
										.append(htm);
								count = count + 1;
								totalCount =totalCount + 1;
							
							}else{
								msg = "Scorecard <strong>"+data[i].templateName+"</strong> rejected, Please fill again. Rejected by:<strong>" + data[i].approvedBy + "</strong>, Contract no "
								+ data[i].contractNo;
								var href = "/vedanta-web/scorecard/user/"
								+ data[i].scorecardId;
						
								htm = "<li> <a   href='"
								+ href + "'>" + msg + "</a> <li>";
								var $ul = $('#ulNotify').addClass("mylist")
								.append(htm);
								totalCount =totalCount + 1;
								
							}
						
							templateId = data[i].templateId;
							scorecardId = data[i].scorecardId
							}	

						} 
						if(totalCount == 5){
							break;
						}

					}

					if (count == 0) {
						$("#countNotification").hide();
					} else {
						 $('#countNotification').removeClass("hidden");
						$("#countNotification").html(count);
					}

				},
				error : function(xhr, status, error) {

					console.log("token has been expired." + xhr.responseText);
				}
			});

}

$("#alert").click(function() {

	var notify = "";

	notify = obj;
	//console.log(notify);

	$("#countNotification").hide();
	if (obj.length > 0) {
		$.ajax({
			type : 'POST',
			dataType : 'json',
			contentType : 'application/json',
			url : "/vedanta-web/notification/checked",
			data : JSON.stringify(notify),
			success : function(result) {
				//console.log(result);

			}
		});
	} else {
		console.log("no notification");
	}

});


