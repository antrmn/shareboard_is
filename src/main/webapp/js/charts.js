$(() => {
    google.load('visualization', '1.0', {'packages':['corechart'], 'callback': drawCharts});
    // google.charts.load('current', {'packages':['corechart']});

    function drawUserBySectionChart(data) {
        // console.log(data);

        let formattedData = new google.visualization.DataTable();
        formattedData.addColumn('string', 'Sezione');
        formattedData.addColumn('number', 'Follow');

        for(let section of Object.entries(data)){
            // console.log(section[1].name)
            // console.log(section[1].nFollowersTotal)
            let name = section[1].name
            let count = section[1].nFollowersTotal
            formattedData.addRow([name, parseInt(count)])
        }

        let options = {
            title: 'Utenti per sezione',
            legendTextStyle: { color: '#FFF' },
            legendTextSize: 20,
            titleTextStyle: { color: '#FFF' },
            backgroundColor: { fill:'transparent' },
            // height: 500,
            // width: 500,
        };

        let chart = new google.visualization.PieChart(document.getElementById('users-bysection-chart'));

        chart.draw(formattedData, options);
    }

    function drawRecentRegistrationsChart(data){
        console.log(data);

        let formattedData = new google.visualization.DataTable();;

        formattedData.addColumn('string', 'Giorno');
        formattedData.addColumn('number', 'Numero');

        for(let x of Object.entries(data)){
            formattedData.addRow([x[0], x[1]])
        }

        let view = new google.visualization.DataView(formattedData);

        let options = {
            title: 'Utenti Iscritti Di Recente',
            legendTextStyle: { color: '#FFF' },
            titleTextStyle: { color: '#FFF' },
            backgroundColor: { fill:'transparent' },
            // height: 500,
            // width: 500,
            bar: {groupWidth: "95%"},
            legend: { position: "none" },
            hAxis : {
                textStyle : {
                    fontSize: 12,
                    baselineColor: '#CCCCCC',
                    color: "#FFFFFF"
                }
            },
            vAxis : {
                textStyle : {
                    fontSize: 12,
                    baselineColor: '#CCCCCC',
                    color: "#FFFFFF"
                }
            }
        };
        let chart = new google.visualization.ColumnChart(document.getElementById("recent-registrations-chart"));
        chart.draw(view, options);
    }

    function drawRecentPostsChart(data){
        console.log(data);

        let formattedData = new google.visualization.DataTable();;

        formattedData.addColumn('string', 'Giorno');
        formattedData.addColumn('number', 'Numero');

        for(let x of Object.entries(data)){
            formattedData.addRow([x[0], x[1]])
        }

        let view = new google.visualization.DataView(formattedData);
        // view.setColumns([0, 1,
        //     { calc: "stringify",
        //         sourceColumn: 1,
        //         type: "string",
        //         role: "annotation" },
        //     2]);

        let options = {
            title: 'Post Recenti',
            legendTextStyle: { color: '#FFF' },
            titleTextStyle: { color: '#FFF' },
            backgroundColor: { fill:'transparent' },
            // height: 500,
            // width: 500,
            bar: {groupWidth: "95%"},
            legend: { position: "none" },
            hAxis : {
                textStyle : {
                    fontSize: 12,
                    baselineColor: '#CCCCCC',
                    color: "#FFFFFF"
                }
            },
            vAxis : {
                textStyle : {
                    fontSize: 12,
                    baselineColor: '#CCCCCC',
                    color: "#FFFFFF"
                }
            }
        };
        let chart = new google.visualization.ColumnChart(document.getElementById("recent-posts-chart"));
        chart.draw(view, options);
    }

    function drawPostBySectionChart(data){
        let formattedData = new google.visualization.DataTable();
        formattedData.addColumn('string', 'Sezione');
        formattedData.addColumn('number', 'Post');

        console.log(data)
        for(let section of Object.entries(data)){
            // console.log(section[1].name)
            // console.log(section[1].nFollowersTotal)
            formattedData.addRow([section[0], section[1]])
        }

        let options = {
            title: 'Post per sezione',
            legendTextStyle: { color: '#FFF' },
            legendTextSize: 20,
            titleTextStyle: { color: '#FFF' },
            backgroundColor: { fill:'transparent' },
            // height: 500,
            // width: 500,
        };

        let chart = new google.visualization.PieChart(document.getElementById('posts-bysection-chart'));

        chart.draw(formattedData, options);
    }

    let save;
    function drawCharts(){
        $.post(
            window.location.origin+"/shareboard/admin",
            function(data){
                save = data
                // console.log(data);
                $(".dashboard-card").removeClass("hide")
                drawUserBySectionChart(JSON.parse(data.section_data));
                drawPostBySectionChart(JSON.parse(data.post_bysection_data))
                drawRecentRegistrationsChart(JSON.parse(data.registration_data))
                drawRecentPostsChart(JSON.parse(data.post_recent_data))
            })
    }
    $(window).resize(function(){
        if(save){
            drawUserBySectionChart(JSON.parse(save.section_data));
            drawPostBySectionChart(JSON.parse(save.post_bysection_data))
            drawRecentRegistrationsChart(JSON.parse(save.registration_data))
            drawRecentPostsChart(JSON.parse(save.post_recent_data))
        }
    });
});