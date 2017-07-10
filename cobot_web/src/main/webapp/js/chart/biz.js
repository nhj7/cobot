google.charts.load('current', {
	'packages' : [ 'corechart' ]
});
google.charts.setOnLoadCallback(drawChart);

function createChart(id) {

	var div = document.createElement("div");
	div.setAttribute("id", id);
	div
			.setAttribute(
					"style",
					"display:table-block;width: 90%px; height: 90%;margin:0 auto;position:relative;");
	
	chartData = new google.visualization.DataTable();
	
	chartData.addColumn('datetime', 'Datum');

	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn({
		type : 'string',
		role : 'tooltip'
	});
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');

	/*
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 */
	chartData.addColumn({
		type : 'string',
		role : 'tooltip'
	});

	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn({
		type : 'string',
		role : 'tooltip'
	});
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');
	chartData.addColumn('number', '');

	/*
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 * chartdata.addColumn('number', ''); chartdata.addColumn('number', '');
	 */
	chartData.addColumn({
		type : 'string',
		role : 'tooltip'
	});
	
	return div;
}

var chart;
var dataTable;
var options;
var chartData;
function drawChart(id, data) {
	
	/*
	 * dataTable = google.visualization.arrayToDataTable([ [ 'Mon', 20, 28, 38,
	 * 45 ], [ 'Tue', 31, 38, 55, 66 ], [ 'Wed', 50, 55, 77, 80 ], [ 'Thu', 77,
	 * 77, 66, 50 ], [ 'Fri', 68, 66, 22, 15 ], [ 'Sun', 77, 55, 22, 15 ] ,[
	 * 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88, 65, 22,
	 * 15 ] ,[ 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88,
	 * 65, 22, 15 ] ,[ 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88, 65, 22, 15 ] ,[
	 * 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88, 65, 22, 15 ] ,[ 'ttt', 88, 65, 22,
	 * 15 ] // Treat first row as data as well. ], true);
	 */
	//alert(data.title);
	
	
	if( chartData != null && chartData != undefined ){
		chartData.removeRows(0, chartData.getNumberOfRows() );
	}
	
	var rowData = [];
	for(var i = 0; i < data.value.length;i++){
		rowData[i] = [ new Date(  parseInt(data.value[i].date) ) ];
		
		for(var j = 0; j < data.value[i].data.length;j++){
			
			if( chartData.getColumnType(j + 1) == "number" ){
				rowData[i].push( parseFloat(data.value[i].data[j]) );
			}else{
				rowData[i].push( data.value[i].data[j] );
			}
			
			
		}
		
		
		
	}
	
	chartData.addRows(rowData);
	var arrTick = [];
	
	for(var  i = 0; i < data.tick.length;i++){
		arrTick.push( parseFloat(data.value.tick) );
	}
	
	if( chart == null || chart == undefined ){
		
		options = {
				chartArea : {
					left : 20,
					top : 20
				},
				width : $("#"+id).width() * 1.1,
				height : '100%',
				fontSize : 11,
				legend : 'none'

				,
				is3D : true,
				bar : {
					groupWidth : '100%'
				} // Remove space between bars.
				,
				candlestick : {
					fallingColor : {
						strokeWidth : 0,
						fill : '#a52714',
						stroke : '#a52714'
					} // red
					,
					risingColor : {
						strokeWidth : 0,
						fill : '#0f9d58',
						stroke : '#0f9d58'
					},
					hollowIsRising : true
				// green
				},
				vAxis : {
					ticks : data.tick
				},
				hAxis : {
					format : 'kk:mm'
				},
				series : {
					0 : {
						color : 'black',
						targetAxisIndex : 0
					},
					1 : {
						color : 'black',
						targetAxisIndex : 1,
					},
					2 : {
						color : 'black'
					},
					3 : {
						color : 'black'
					}
				},
				vAxes : {
					0 : {
						textPosition : 'none'
					},
					1 : {}
				}

				,
				title : data.title

				,
				titleTextStyle : {
					color : 'black', // any HTML string color ('red', '#cc00cc')
					fontName : 'Times New Roman', // i.e. 'Times New Roman'
					fontSize : '12em', // 12, 18 whatever you want (don't specify px)
					bold : true, // true or false
					italic : false
				// true of false
				}

				,
				legendTextStyle : {
					color : 'black', // any HTML string color ('red', '#cc00cc')
					fontName : 'Times New Roman', // i.e. 'Times New Roman'
					fontSize : '12em', // 12, 18 whatever you want (don't specify px)
					bold : true, // true or false
					italic : false
				// true of false
				},
				annotations : {
					alwaysOutside : true,
					textStyle : {
						color : '#000000',
						fontSize : 12
					}
				}

			};
		
		chart = new google.visualization.CandlestickChart(document
				.getElementById(id));
		chart.draw(chartData, options);
		window.addEventListener('resize', function() {
			// alert();
			options.width = $("#"+id).width() * 1.1;
			chart.draw(chartData, options);
		}, false);
	}else{
		options.title = data.title;
		options.vAxis.ticks = data.tick;
		chart.draw(chartData, options);
		
	}
	
	

	

	// setTimeout("dataTable.addRows( [[ 'Sat', 68, 66, 22, 15 ]] );
	// chart.draw(dataTable, options);",1000);
}