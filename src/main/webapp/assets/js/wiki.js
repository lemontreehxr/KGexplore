function getTitleRequest(query){
    var url = "https://en.wikipedia.org/w/api.php?action=opensearch&format=json&limit=1&callback=?&search=" + query;
    return $.ajax({
        url: url,
        type: 'GET',
        contentType: "application/json; charset=utf-8",
        async: false,
        dataType: "json"
    });
}

function getImageRequest(query){
    var url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=pageimages&piprop=original&callback=?&titles=" + query;
    return $.ajax({
        url: url,
        type: 'GET',
        contentType: "application/json; charset=utf-8",
        async: false,
        dataType: "json"
    });
}

function getImageUrl(name) {
    //var extract = document.createElement("p");
    //extract.setAttribute("style", "margin-top: 2px");
    var url = "assets/img/not_found.jpeg";
    getTitleRequest(name).success(function(data){
        //extract.innerHTML = data[2][0];
        //console.log(data[1][0]);
        getImageRequest(data[1][0]).success(function(data){
            var pages = data.query.pages;
            for (var page in pages) {
                if (pages.hasOwnProperty(page)) {
                    var pdata = pages[page];
                    if(pdata.thumbnail)
                        url = pdata.thumbnail.original;
                }
            }
        });
    })

    return url;
}