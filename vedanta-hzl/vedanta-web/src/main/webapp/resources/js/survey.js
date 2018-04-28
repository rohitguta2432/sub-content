$(document).ready(
		function() {
		
			
			
			$('#plantId').on('change', function() {
				var value = this.value;

				if (value > 0) {
					$('#plantmsg').hide();
				}

				$.ajax({
					url : "/vedanta-web/email/getVendorDetails/" + value,
					type : "GET",
					success : function(data) {
						showVendorData(data);
					},
					error : function(xhr, status, error) {

					}
				});
			})

			$('#vendorId').on('change', function() {
				var value = this.value;

				if (value > 0) {
					$('#vendormsg').hide();
				}

				$.ajax({
					url : "/vedanta-web/email/getContractDetails/" + value,
					type : "GET",
					success : function(data) {
						showContractData(data);
					},
					error : function(xhr, status, error) {

					}
				});
			})

			$('#contractId').on('change', function() {
				var value = this.value;

				if (value > 0) {
					$('#contractmsg').hide();
				}

				$.ajax({
					url : "/vedanta-web/email/getSurveyDetails/" + value,
					type : "GET",
					success : function(data) {
						showSurveyData(data);
					},
					error : function(xhr, status, error) {

					}
				});
			})
			$('#formId').on('change', function() {
				var value = this.value;

				if (value > 0) {
					$('#surveymsg').hide();
				}
			});

			$('#scorecard-users').on('change', function() {
				var value = this.value;

				if (value.length > 0) {
					$('#usermsg').hide();
				}
			});

			function showVendorData(data) {
				$('#vendorId').html("");
				$('#vendorId').append(
						"<option value='0'>Select vendor </option>");
				for (var i = 0; i < data.length; i++) {
					$('#vendorId').append(
							"<option value=\"" + data[i].id + "\">"
									+ data[i].name + "</option>");
				}
				$('#vendorId').selectpicker({
					liveSearch : true,
					maxOptions : 1
				});
				$('#vendorId').selectpicker('render');
				$("#vendorId").val('').selectpicker('refresh');
			}

			function showContractData(data) {
				$('#contractId').html("");
				$('#contractId').append(
						"<option value='0'>Select contract </option>");
				for (var i = 0; i < data.length; i++) {
					$('#contractId').append(
							"<option value=\"" + data[i].id + "\">"
									+ data[i].number + "</option>");
				}
				$('#contractId').selectpicker({
					liveSearch : true,
					maxOptions : 1
				});
				$('#contractId').selectpicker('render');
				$("#contractId").val('').selectpicker('refresh');
			}

			function showSurveyData(data) {
				$('#formId').html("");
				$('#formId')
						.append("<option value='0'>Select survey </option>");
				for (var i = 0; i < data.length; i++) {
					$('#formId').append(
							"<option value=\"" + data[i].id + "\">"
									+ data[i].name + "</option>");
				}
				$('#formId').selectpicker({
					liveSearch : true,
					maxOptions : 1
				});
				$('#formId').selectpicker('render');
				$("#formId").val('').selectpicker('refresh');
			}

		});
