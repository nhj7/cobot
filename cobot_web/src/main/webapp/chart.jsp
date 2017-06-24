<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>

<script type="text/javascript" src="/js/chart/loader.js"></script>
<script type="text/javascript">
	google.charts.load('current', {
		'packages' : [ 'corechart' ]
	});
	google.charts.setOnLoadCallback(drawChart);
	
	function createDiv(id){
		
		var div = document.createElement("div");
		div.setAttribute("id", id);
		div.setAttribute("style", "display:table-block;width: 90%px; height: 60%;margin:0 auto;");
		
		return div;
	}
	
	var chart;
	var dataTable;	
	var options;
	function drawChart() {
		
		/*
		dataTable = google.visualization.arrayToDataTable([
			[ 'Mon', 20, 28, 38, 45 ], [ 'Tue', 31, 38, 55, 66 ],
			[ 'Wed', 50, 55, 77, 80 ], [ 'Thu', 77, 77, 66, 50 ],
			[ 'Fri', 68, 66, 22, 15 ], [ 'Sun', 77, 55, 22, 15 ]
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
			,[ 'ttt', 88, 65, 22, 15 ] 
	// Treat first row as data as well.
	], true);
	*/
	
	var chartdata = new google.visualization.DataTable();
	chartdata.addColumn('datetime', 'Datum');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn({type: 'string', role: 'tooltip'});
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	
	/*
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	chartdata.addColumn('number', '');
	*/
	chartdata.addColumn({type: 'string', role: 'tooltip'});
	
	chartdata.addRows([
	    [new Date(2014, 3, 1, 01, 00, 00), (0.00009239),(0.00009139),(0.00009339),(0.00009039), 'tooltip!!' , (0.00009839),(0.00009739),(0.00009850),(0.00009830), '$600K in our first year!'],
	    [new Date(2014, 3, 1, 02, 00, 00),  (0.00009839),(0.00009739),(0.00009650),(0.00009530), 'tooltip!!' ,(0.00009839),(0.00009739),(0.00009850),(0.00009830),'$600K in our first year!'],
	    [new Date(2014, 3, 1, 03, 00, 00),  (0.00009839),(0.00009739),(0.00009850),(0.00009830), 'tooltip!!', (0.00009839),(0.00009739),(0.00009850),(0.00009830),'$600K in our first year!'],
	    [new Date(2014, 3, 1, 04, 00, 00),  (0.00009839),(0.00009739),(0.00009850),(0.00009830), 'tooltip!!', (0.00009839),(0.00009739),(0.00009850),(0.00009830),'$600K in our first year!'],
	    [new Date(2014, 3, 1, 05, 00, 00),  (0.00009839),(0.00009739),(0.00009850),(0.00009830), 'tooltip!!' ,(0.00009839),(0.00009739),(0.00009850),(0.00009830),'$600K in our first year!'],
	]);
	
	//alert(11);
	

		
		options = {
			fontSize : 18
			, legend : 'none'
			
			, is3D : true
			, bar : {
				groupWidth : '100%'
			} // Remove space between bars.
			, candlestick : {
				fallingColor : {
					strokeWidth : 0
					, fill : '#a52714'
					, stroke : '#a52714'
				} // red
				,risingColor : {
					strokeWidth : 0
					, fill : '#0f9d58'
					, stroke : '#0f9d58'
				} 
				,hollowIsRising: true
			// green
			}
			, vAxis: { ticks :[ 0.00009000, 0.00009200, 0.00009400, 0.00009600, 0.00009800, 0.00010000]}
			
			,series: {
	            0: {color: 'black' , targetAxisIndex: 0}
	            , 1: {color: 'black' , targetAxisIndex: 1, }
	            , 2: {color: 'black'}
	            , 3: {color: 'black'}
	        }
			, vAxes: {
                0: { textPosition: 'none' },
                1: {}
            }

			, title : "ETH/BTC"
			
			,titleTextStyle: {
		        color: 'black',    // any HTML string color ('red', '#cc00cc')
		        fontName: 'Times New Roman', // i.e. 'Times New Roman'
		        fontSize: '12em', // 12, 18 whatever you want (don't specify px)
		        bold: true,    // true or false
		        italic: false   // true of false
		    }
			
			, legendTextStyle : {
		        color: 'black',    // any HTML string color ('red', '#cc00cc')
		        fontName: 'Times New Roman', // i.e. 'Times New Roman'
		        fontSize: '12em', // 12, 18 whatever you want (don't specify px)
		        bold: true,    // true or false
		        italic: false   // true of false
		    }
			, annotations: {
                alwaysOutside: true,
                textStyle: {
                    color: '#000000',
                    fontSize: 15
                }
            }

		};

		chart = new google.visualization.CandlestickChart(document
				.getElementById('chart_div'));
		
		chart.draw(chartdata, options);
		
		window.addEventListener('resize', function() {
			chart.draw(chartdata, options);
	    }, false);
		
		
		//setTimeout("dataTable.addRows( [[ 'Sat', 68, 66, 22, 15 ]] ); chart.draw(dataTable, options);",1000);
	}
</script>
</head>

<body>
	<!--Div that will hold the pie chart-->

</body>
</html>