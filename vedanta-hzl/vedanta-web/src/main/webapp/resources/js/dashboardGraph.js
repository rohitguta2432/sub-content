$(document).ready(function(){
	
// for grap1

/*var janAvg=[];
var febAvg=[];
var marAvg=[];
var aprilAvg=[];
var mayAvg=[];
var junAvg=[];
var julyAvg=[];
var augAvg=[];
var septAvg=[];
var octAvg=[];
var novAvg=[];
var decAvg=[];


// for graph2
// for finalvendorDetails
let vendorDetails=[];
//console.log(vendorDetails);

let vendorArray=[];
// console.log(vendorArray);

let monthWithYear=[];
//console.log(monthWithYear);



$.each(dashbordData.monthList, function(key, value) {
	// console.log(value);

	let currentMonthId=value.monthId;
	let currentyearId=value.yearId;
	
   			
$.each(dashbordData.newDashboardDetails, function(key, value1) {
		// console.log(value1);

$.each(value1,function(key,value2){
	// console.log(value2);
	
	$.each(value2,function(key,value3){
	 	// console.log(key);
		// console.log(value3);
		
	 	$.each(value3,function(key,value4){
	 		// console.log(key);
	 
		
			$.each(value4,function(key,value5){
    	 	 	// console.log(key);
    			// console.log(value5);
    	
    			
    			let monthId=value5['monthId'];
    			let sumActualscore=value5['sumActualScore'];
    			let totalScore=value5['totalScore'];
    			let avgActualScore=value5['avgActualScore'];
    			let contractNumber=value5['contractNumber'];
    			let vendorId=value5['vendorId'];
    			let yearId=value5['yearId'];
    			
    			
    			if (monthId === currentMonthId && yearId === currentyearId){
    					// console.log('monthId--'+ monthId +'currentMonthId--
						// '+currentMonthId +'yearId-- '+yearId
						// +'currentYearId-- '+currentyearId )
    				// for jan
	    			if (monthId === 0){
	    				if (sumActualscore>totalScore){
		    				
		    				janAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
			    					"contractNumber":contractNumber,
			    					"finalValue":avgActualScore,
			    					"monthId":monthId,
			    					"vendorId":vendorId
			    					});
		    				monthWithYear.push('jan' +'-'+yearId);
		    				
		    			} else{
		    				
		    				janAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('jan' +'-'+yearId);
		    				}
	    				}
	    			
	    			// for feb
	    			if (monthId === 1){
	    				if (sumActualscore>totalScore){
	    				
		    				febAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('feb' +'-'+yearId);
		    				
		    			} else{
		    				
		    				febAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('feb' +'-'+yearId);
		    				}
	    				}
	
	    			
	    			// for march
	    			if (monthId === 2){
	    				if (sumActualscore>totalScore){
		    				
		    				marAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('mar' +'-'+yearId);
		    				
		    			} else{
		    				
		    				marAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('mar' +'-'+ yearId);
		    				}
	    				}
	    			
	    			
	    			// for april
	    			if (monthId === 3){
	    				if (sumActualscore>totalScore){
		    				
		    				aprilAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('apr'+'-'+yearId);
		    			} else{
		    				
		    				aprilAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('apr'+'-'+yearId);
		    				}
	    				}
	    			
	    			
	    			// for may
	    			if (monthId === 4){
	    				if (sumActualscore>totalScore){
		    				
		    				mayAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('may' +'-'+yearId);
		    			} else{
		    				
		    				mayAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('may' +'-'+ yearId);
		    				}
	    				}
	    			
	    			
	    			// for jun
	    			if (monthId === 5){
	    				
	    				if (sumActualscore>totalScore){
		    				
		    				junAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('jun' + '-' + yearId);
		    			} else{
		    				
		    				junAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('jun'+'-'+yearId);
		    				}
	    				}
	    			
	    			// for july
	    			if (monthId === 6){
	    				if (sumActualscore>totalScore){
		    				
		    				julyAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('july'+'-'+yearId);
		    			}else{
		    				
		    				julyAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('july' +'-'+yearId);
		    				}
	    				}
	    			
	    			// for aug average
	    			if (monthId === 7){
	    				if(sumActualscore>totalScore){
		    				
		    				augAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('aug'+'-'+yearId);
		    			}else{
		    				
		    				augAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('aug'+ '-'+ yearId);
		    				}
	    				}
	    			
	    			
	    			// for sept
	    			if (monthId === 8){
	    				if(sumActualscore>totalScore){
		    				
		    				septAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('sept'+'-'+yearId);
		    				
		    			}else{
		    				
		    				septAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('sept'+'-'+yearId);
		    				}
	    				}
	    			
	    			// for oct
	    			if(monthId === 9){
	    				if(sumActualscore>totalScore){
		    			
		    				octAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('oct'+'-'+yearId);
		    			}else{
		    				
		    				octAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('oct'+'-'+yearId);
		    				}
	    				}
	    			
	    			// for nov
	    			if(monthId === 10){
	    				if(sumActualscore>totalScore){
		    				
		    				novAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('nov'+'-'+yearId);
		    			}else{
		    				
		    				novAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('nov'+'-'+yearId);
		    				}
	    				}
	    			
	    			// for dec
	    			if(monthId === 11){
	    				if(sumActualscore>totalScore){
		    				
		    				decAvg.push(avgActualScore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":avgActualScore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('dec'+'-'+yearId);
		    			}else{
		    				
		    				decAvg.push(sumActualscore);
		    				
		    				vendorDetails.push({
		    					"contractNumber":contractNumber,
		    					"finalValue":sumActualscore,
		    					"monthId":monthId,
		    					"vendorId":vendorId
		    					});
		    				monthWithYear.push('dec' +'-'+yearId);
				    				}
			    				}
		    				}
		    			
		    			});  
	    			
	 				}); 
	    		
	    		});
	    
	    	});
	    
		});
  });
  



  // for Average of All Months
let janVal=eval(janAvg.join('+'))/janAvg.length;
let febVal=eval(febAvg.join('+'))/febAvg.length;
let marVal=eval(marAvg.join('+'))/marAvg.length;
let aprilVal=eval(aprilAvg.join('+'))/aprilAvg.length;  
let mayVal=eval(mayAvg.join('+'))/mayAvg.length;
let junVal=eval(junAvg.join('+'))/junAvg.length;
let julyVal=eval(julyAvg.join('+'))/julyAvg.length;
let augtVal=eval(augAvg.join('+'))/augAvg.length;
let septVal=eval(septAvg.join('+'))/septAvg.length;
let octVal=eval(octAvg.join('+'))/octAvg.length;
let novVal=eval(novAvg.join('+'))/novAvg.length;
let decVal=eval(decAvg.join('+'))/decAvg.length;


const uniquemonthWithYear = Array.from(new Set(monthWithYear));

vendorDetails.forEach(function(graph1Details){
	
	const finalvalue=graph1Details.finalValue;
	const monthId=graph1Details.monthId;
	//console.log(finalvalue);
	//console.log(monthId);
	
});



const Datacharts=[];

if(janVal!=null && !Number.isNaN(janVal)){
	Datacharts.push(janVal);
}
if(febVal!=null && !Number.isNaN(febVal)){
	Datacharts.push(febVal);
}
if(marVal!=null && !Number.isNaN(marVal)){
	Datacharts.push(marVal);
}
if(aprilVal!=null && !Number.isNaN(aprilVal)){
	Datacharts.push(aprilVal);
}
if(mayVal!=null && !Number.isNaN(mayVal)){
	Datacharts.push(mayVal);
}
if(junVal!=null && !Number.isNaN(junVal)){
	Datacharts.push(junVal);
}
if(julyVal!=null && !Number.isNaN(julyVal)){
	Datacharts.push(julyVal);
}
if(augtVal!=null && !Number.isNaN(augtVal)){
	Datacharts.push(augtVal);
}
if(septVal!=null && !Number.isNaN(septVal)){
	Datacharts.push(septVal);
}
if(octVal!=null && !Number.isNaN(octVal)){
	Datacharts.push(octVal);
}
if(novVal!=null && !Number.isNaN(novVal)){
	Datacharts.push(novVal);
}
if(decVal!=null && !Number.isNaN(decVal)){
	Datacharts.push(decVal);
}


// after finalVendorDetails extract contractNumber with its average of
// finalValue
let	hash = Object.create(null);

// extracted value from vendorDetails and it average
let vendorDetailsResult = [];

vendorDetails.forEach(({ contractNumber, finalValue,vendorId }) => {
	hash[contractNumber] = hash[contractNumber] || { index: vendorDetailsResult.push({ contractNumber, average: 0,vendorId:vendorId }) - 1, sum: 0, count: 0 };
	hash[contractNumber].sum += finalValue;
	vendorDetailsResult[hash[contractNumber].index].average = hash[contractNumber].sum / ++hash[contractNumber].count;
	});

// for Average of similar vendorId
let vendorDetailsAverageData=[];

	vendorDetailsResult.forEach(({ vendorId,average }) => {
	hash[vendorId] = hash[vendorId] || { index: vendorDetailsAverageData.push({ vendorId, finalAverage: 0}) - 1, sum: 0, count: 0 };
	hash[vendorId].sum += average;
	vendorDetailsAverageData[hash[vendorId].index].finalAverage = hash[vendorId].sum / ++hash[vendorId].count;
	});  

if(vendorDetailsResult.length===0){
	$('#graph2').highcharts({
responsive: {
    rules: [{
        condition: {
            maxWidth: 500
        },
        chartOptions: {
            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom'
            }
        }
    }]
},
title: {
    text: 'Performance of Vendors'
},
xAxis: {
    categories:[],
   	crosshair: true
}, 
yAxis:{
    min: 0, max: 100,

    lineColor: '#FF0000',
    lineWidth: 1,
    title: {
        text: 'Vendor Performance'
    }
},
tooltip: {
    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
        '<td style="padding:0"><b>{point.y:.2f} </b></td></tr>',
    footerFormat: '</table>',
        shared: true,
        useHTML: true,
    }, 
  plotOptions: {
        series: {
        	lineColor: '#303030',
        dataLabels: {
            enabled: true,
     
           formatter: function () {
                return Highcharts.numberFormat(this.y,2);
            }, 
           
        },
       
    }
}, 
series: [{
    name: 'vendor performance',
	        data: []
	    
		  }]

	}); 

}
	
 
 const vendorName=[];
 
let vendorAverageperformance=[];
	
for(var i=0;i<vendorDetailsAverageData.length;i++){
	
	let vendorId=vendorDetailsAverageData[i].vendorId;
	let average=vendorDetailsAverageData[i].finalAverage;
	
	vendorAverageperformance.push(average);
	

	$.ajax({
        url : "/vedanta-web/web/dashboard/getVendorByVendorId/"+vendorId,
type : "GET",
success : function(data) {
	
	vendorName.push(data.name);

	$('#graph2').highcharts({
	
	responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    },
	    title: {
	        text: 'Performance of Vendors'
	    },
	
	    xAxis: {
	        categories:vendorName,
	       	crosshair: true
	    }, 
	    yAxis:{
            min: 0, max: 100,

            lineColor: '#FF0000',
            lineWidth: 1,
            title: {
                text: 'Vendor Performance'
            }
        },
	    tooltip: {
	        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:.2f} </b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    }, 
	  plotOptions: {
	        series: {
	        	lineColor: '#303030',
	            dataLabels: {
	                enabled: true,
	         
	               formatter: function () {
	                    return Highcharts.numberFormat(this.y,2);
	                }, 
	               
	            },
	           
	        }
	    }, 
	    series: [{
	        name: 'vendor performance',
    		        data: vendorAverageperformance
    		        }]
    		}); 
        		
       },
        error : function(xhr, status, error) {
    
     	}
	});  
	
} 


// for first graph
Highcharts.chart('graph1', {

title: {
    text: 'Performance of categories-subcategories across months'
    },

	xAxis: {
        categories:uniquemonthWithYear
    },
  
    yAxis:{
        min: 0, max: 100,

        lineColor: '#FF0000',
lineWidth: 1,
title: {
    text: 'Performance of categories-subcategories'
    }
},
tooltip: {
    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
    '<td style="padding:0"><b>{point.y:.2f} </b></td></tr>',
footerFormat: '</table>',
    shared: true,
    useHTML: true
},
plotOptions: {
    series: {
        dataLabels: {
            enabled: true,
     
            formatter: function () {
                return Highcharts.numberFormat(this.y,2);
            }, 
        },
        
    }
},

series: [{
	 name: 'Average Performance of categories-subcategories',
	        data: Datacharts
	    
	    }]

	});

for no graph information data exits or not 

var chart1=$('#graph1').highcharts();
var chart2=$('#graph2').highcharts();

if(chart1.series[0].data.length<1){
	
	 chart1.renderer.text('No Data Available', 250, 170)
      .css({
    	  color: '#4572A7',
          fontSize: '14px'
      })
      .add();
}


	if(chart2.series[0].data.length<1){

		chart2.renderer.text('No Data Available', 250, 170)
	      .css({
	          color: '#4572A7',
	          fontSize: '14px'
	      })
	      .add();
}*/

});