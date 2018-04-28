/* Custom JS File*/
/*$g = jQuery.noConflict();
 $g(function() {
 // alert('asas');
 });
 */
function showAlert(message, type) {

	var alertTitle = null;

	if (type == "Error" || type == "error") {
		alertTitle = "Alert!!!";
	} else if (type == "Warning" || type == "warning") {
		alertTitle = "Warning!!!";
	} else {
		alertTitle = "Success!!!";
	}

	$("#alertTitle").text(alertTitle);

	$("#cbAlertText").text(message);
	$("#cbAlertText").css("bold");
	$("#cbAlert").modal('show');
}

function showConfirmation(message) {

	$("#cbModalText").text(message);
	$("#cbModalText").css("bold");
	$("#cbModal").modal('show');
}

function showSpinner() {
	$('#spinner').css('display', 'block');
}

function hideSpinner() {
	$('#spinner').css('display', 'none');
}

function getIndustryNames() {

	showSpinner();
	var url = "/vpmt-web/web/industry/names";

	$.ajax({
		type : "GET",
		url : url,
		success : function(response) {
			var status = response['status'];
			if (status == 200) {
				populateIndustryDropDown(response['data']);
				hideSpinner();
				window.location.reload();
			} else {
				showAlert("Error while fetching industry details.", "Error");
				hideSpinner();
			}
		},
		failure : function() {
			showAlert("Error while fetching industry details.", "Error");
			hideSpinner();
			return;
		},
		error : function() {
			showAlert("Error while fetching industry details.", "Error");
			hideSpinner();
			return;
		}
	});
}

function populateIndustryDropDown(data) {

	var options = $g("#industryDropDown");
	$.each(data, function(item) {
		options.append($g("<option />").val(item.id).text(item.Name));
	});
}

function getClientsByIndustry(industryId) {

	showSpinner();
	var url = "/vpmt-web/web/client/" + industryId;
	var result = "";
	$
			.ajax({
				type : "GET",
				url : url,
				async : false,
				success : function(response) {
					var status = response['status'];
					if (status == 200) {
						hideSpinner();
						result = response['data'];
					} else {
						showAlert("Error while fetching clients by industry.",
								"Error");
						hideSpinner();
					}
				},
				failure : function() {
					showAlert("Error while fetching clients by industry",
							"Error");
					hideSpinner();
					return;
				},
				error : function() {
					showAlert("Error while fetching industry details.", "Error");
					hideSpinner();
					return;
				}
			});
	return result;
}
var confirmMsg = "Do you really want to push the data into Sherlock."
var noMsg = "Do you really want to abort the upload";

function approveIndustryData() {

	if (confirm(confirmMsg) == true) {

		showSpinner();
		var url = "/vpmt-web/web/industry/approve";
		var result = "";
		$.ajax({
			type : "POST",
			url : url,
			async : false,
			timeout : 1000000,
			success : function(response) {
				var status = response['status'];
				if (status == 200) {
					hideSpinner();
					result = response['data'];
				} else {
					showAlert("Error while approving industry data.", "Error");
					hideSpinner();
				}
			},
			failure : function() {
				showAlert("Error while approving industry data.", "Error");
				hideSpinner();
				return;
			},
			error : function() {
				showAlert("Error while approving industry data.", "Error");
				hideSpinner();
				return;
			}
		});

		if (result == true) {
			alert("Data has been successfully pushed to Sherlock.");
			window.location.href = '/vpmt-web/web/industry';
		} else {
			alert("Error while saving Industry data.");
		}
	}

}

function deleteEntryFromServer(entryName) {

	var url = "/vpmt-web/reject/" + entryName;
	$.ajax({
		type : "GET",
		url : url,
		async : false,
		success : function(response) {
			var status = response['status'];
			if (status == 200) {
				hideSpinner();
				result = response['data'];
			} else {
				showAlert("Error while making request.", "Error");
				hideSpinner();
			}
		},
		failure : function() {
			showAlert("Error while making request.", "Error");
			hideSpinner();
			return;
		},
		error : function() {
			showAlert("Error while making request.", "Error");
			hideSpinner();
			return;
		}
	});

}

function noapproveIndustryData() {
	deleteEntryFromServer("industry-sheet");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/industry';
	}
}

function approveClientData() {
	if (confirm(confirmMsg) == true) {
		showSpinner();
		// var url = "/vpmt-web/web/client/approve";
		window.location.href = '/vpmt-web/web/client/approve';
		/*
		 * var result = ""; $.ajax({ type : "POST", url : url, async : false,
		 * timeout: 1000000, success : function(response) { var status =
		 * response['status']; if (status == 200) { hideSpinner(); result =
		 * response['data']; } else { showAlert("Error while approving client
		 * data.", "Error"); hideSpinner(); } }, failure : function() {
		 * showAlert("Error while approving client data.", "Error");
		 * hideSpinner(); return; }, error : function() { showAlert("Error while
		 * approving client data.", "Error"); hideSpinner(); return; } });
		 * 
		 * if (result == true) { alert("Data has been successfully pushed to
		 * Sherlock."); window.location.href='/vpmt-web/web/client'; }
		 * else { alert("Error while saving client data successfully."); }
		 */

	}
}

function noapproveClientData() {
	
	deleteEntryFromServer("client-sheet");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/client';
	}

}

function approveClientBusinessFlowData() {

	if (confirm(confirmMsg) == true) {

		showSpinner();
		var url = "/vpmt-web/web/client/business-flow/approve";
		var result = "";
		$.ajax({
			type : "POST",
			url : url,
			async : false,
			timeout : 1000000,
			success : function(response) {
				var status = response['status'];
				if (status == 200) {
					hideSpinner();
					result = response['data'];
				} else {
					showAlert(
							"Error while approving client business flow data.",
							"Error");
					hideSpinner();
				}
			},
			failure : function() {
				showAlert("Error while approving client business flow data.",
						"Error");
				hideSpinner();
				return;
			},
			error : function() {
				showAlert("Error while approving client business flow data.",
						"Error");
				hideSpinner();
				return;
			}
		});

		if (result == true) {
			alert("Data has been successfully pushed to Sherlock.");
			window.location.href = '/vpmt-web/web/client/business-flow';
		} else {
			alert("Error while saving client business flow data.");
		}
	}
}

function noapproveClientBusinessFlowData() {
	deleteEntryFromServer("ClientBusinessFlow");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/client/business-flow';
	}

}

function approveProductData() {
	if (confirm(confirmMsg) == true) {
		showSpinner();
		var url = "/vpmt-web/web/product/approve";
		var result = "";
		$.ajax({
			type : "POST",
			url : url,
			async : false,
			timeout : 1000000,
			success : function(response) {

				var status = response['status'];
				if (status == 200) {
					hideSpinner();
					result = response['data'];
					alert(result);
					window.location.href = '/vpmt-web/web/product';
				} else {
					showAlert("Error while approving client data.", "Error");
					hideSpinner();
				}
			},
			failure : function() {
				showAlert("Error while approving client data.", "Error");
				hideSpinner();
				return;
			},
			error : function() {
				showAlert("Error while approving client data.", "Error");
				hideSpinner();
				return;
			}
		});

	}
}

function noapproveProduct() {
	deleteEntryFromServer("Product");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/product';
	}

}

function approvePricingData() {
	if (confirm(confirmMsg) == true) {
		showSpinner();
		var url = "/vpmt-web/web/pricing/approve";
		var result = "";
		$.ajax({
			type : "POST",
			url : url,
			async : false,
			timeout : 1000000,
			success : function(response) {
				var status = response['status'];
				if (status == 200) {
					hideSpinner();
					result = response['data'];
					alert(result);
					window.location.href = '/vpmt-web/web/pricing';
				} else {
					showAlert("Error while approving pricing data.", "Error");
					hideSpinner();
				}
			},
			failure : function() {
				showAlert("Error while approving pricing data.", "Error");
				hideSpinner();
				return;
			},
			error : function() {
				showAlert("Error while approving pricing data.", "Error");
				hideSpinner();
				return;
			}
		});
	}
}

function noapprovePricing() {
	deleteEntryFromServer("Pricing");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/pricing';
	}

}

function approveTargetData() {
	if (confirm(confirmMsg) == true) {
		showSpinner();
		var url = "/vpmt-web/web/target/approve";
		var result = "";
		$.ajax({
			type : "POST",
			url : url,
			async : false,
			timeout : 1000000,
			success : function(response) {
				var status = response['status'];
				if (status == 200) {
					hideSpinner();
					result = response['data'];
					alert(result);
					window.location.href = '/vpmt-web/web/target';
				} else {
					showAlert("Error while approving target data.", "Error");
					hideSpinner();
				}
			},
			failure : function() {
				showAlert("Error while approving target data.", "Error");
				hideSpinner();
				return;
			},
			error : function() {
				showAlert("Error while approving target data.", "Error");
				hideSpinner();
				return;
			}
		});
	}
}

function noapproveTarget() {
	deleteEntryFromServer("Target");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/target';
	}

}

function approveConstructionData() {
	if (confirm(confirmMsg) == true) {
		showSpinner();
		var url = "/vpmt-web/web/performance/leadstatus/approve";
		var result = "";
		$
				.ajax({
					type : "POST",
					url : url,
					async : false,
					timeout : 1000000,
					success : function(response) {
						var status = response['status'];
						if (status == 200) {
							hideSpinner();
							result = response['data'];
							alert(result);
							window.location.href = '/vpmt-web/web/performance/leadstatus';
						} else {
							showAlert(
									"Error while approving lead-Status.",
									"Error");
							hideSpinner();
						}
					},
					failure : function() {
						showAlert("Error while approving lead-Status.",
								"Error");
						hideSpinner();
						return;
					},
					error : function() {
						showAlert("Error while approving lead-Status.",
								"Error");
						hideSpinner();
						return;
					}
				});
	}
}

function noapproveConstruction() {
	deleteEntryFromServer("LeadStatus");
	if (confirm(noMsg) == true) {
		window.location.href = '/vpmt-web/web/performance/leadstatus';
	}

}

function getProductByLevel(levelId) {
	showSpinner();
	var url = "/vpmt-web/web/product/level/" + levelId;
	var result = "";
	$.ajax({
		type : "GET",
		url : url,
		async : false,
		success : function(response) {
			var status = response['status'];
			if (status == 200) {
				hideSpinner();
				result = response['data'];
			} else {
				showAlert("Error while fetching product by level : " + levelId,
						"Error");
				hideSpinner();
			}
		},
		failure : function() {
			showAlert("Error while fetching product by level : " + levelId,
					"Error");
			hideSpinner();
			return;
		},
		error : function() {
			showAlert("Error while fetching product by level : " + levelId,
					"Error");
			hideSpinner();
			return;
		}
	});
	return result;
}

// Mapping data of drop down
var map = new Object(); // or var map = {};
map['Industry'] = 'Industry Insights';
map['CustomerProgress'] = 'Progress';
map['DocumentChecklist'] = 'Document Check List';
map['Client'] = 'Client Insights';
map['Product_Pricing'] = 'Product and Pricing';
map['reports'] = 'Report and News';
map['insight'] = 'Insight';
map['macro'] = 'Macro';
map['archetype'] = 'Archetype';
map['segment'] = 'Segment Insight';
map['industry'] = 'Industry Insight';

map['news'] = 'News';
map['financials'] = 'Financials';
map['insight'] = 'Insight';
map['client_profile'] = 'Client Profile';
map['shareholder'] = 'Share Holder';
map['business_flow'] = 'Business Flow';
map['wc_diagnostic'] = 'Wc_Diagnostic';
map['scb_relationship_with_client'] = 'Value Chain';
map['customerprogress'] = 'Customer Progress';
map['documentchecklist'] = 'Document Check List';
map['product_pricing'] = 'Product and Pricing';
map['share'] = 'Share';
map['NOTE'] = 'RM Notes';
map['AUTOMATED'] = 'System Generated Notes';
map['ADMIN_SHARE'] = 'Admin Notes';
map['FIXED'] = 'Talking Points';
map['Progress'] = 'Progress';


function getValue(k) {
	return map[k];
}

function getBenchMarkByProductId() {
    showSpinner();
    var url = "/vpmt-web/web/benchmarklogo/benchmark";
    var result = "";
    $.ajax({
        type : "GET",
        url : url,
        async : false,
        success : function(response) {
            var status = response['status'];
            if (status == 200) {
                hideSpinner();
                result = response['data'];
            } else {
                showAlert("Error while fetching benchmark by level : " + productId,
                    "Error");
                hideSpinner();
            }
        },
        failure : function() {
            showAlert("Error while fetching benchmark by level : " + productId,
                "Error");
            hideSpinner();
            return;
        },
        error : function() {
            showAlert("Error while fetching benchmark by level : " + productId,
                "Error");
            hideSpinner();
            return;
        }
    });
    return result;
}


$(function () {

	$('#sidebar-menu, #customize-menu').metisMenu({
		activeClass: 'open'
	});


	$('#sidebar-collapse-btn').on('click', function(event){
		event.preventDefault();
		
		$("#app").toggleClass("sidebar-open");
	});

	$("#sidebar-overlay").on('click', function() {
		$("#app").removeClass("sidebar-open");
	});
	
});

$(function() {

	$("body").addClass("loaded");
		
	$('.expand-toggle').on('click',function(e){
		 $('.preview-imaes').slideToggle(400);
	});

});



/*var selector = '.nav li';

$('.slider').on('click', function(){
    $('.slider').removeClass('active');
    $(this).addClass('active');
});*/
/*
$(function() {
	$("li").click(function() {
	      // remove classes from all
	      $("li").removeClass("active");
	      // add class to the one we clicked
	      $(this).addClass("active");
	   });
	});*/