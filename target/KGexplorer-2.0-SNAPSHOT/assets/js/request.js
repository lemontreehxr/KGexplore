var request_prefix = "http://10.77.110.227:8080/KGexplorer_war/";
//var request_prefix = "http://localhost:8080/";


function getResultRequest(queryContent){
    var url = request_prefix + "controller/getResult?" + queryContent;
    console.log(url);
    return $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        async: true,
        dataType: "json"
    });
}

function getProfileRequest(queryContent){
    var url = request_prefix + "controller/getProfile?" + queryContent;
    console.log(url);
    return $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        async: true,
        dataType: "json"
    });
}

function getAssessRequest(user_id){
    var url = request_prefix + "controller/getAssess?userId=" + user_id;
    console.log(url);
    return $.ajax({
        url: url,
        type: "GET",
        timeout: 10000,
        contentType: "application/json; charset=utf-8",
        async: true,
        dataType: "json"
    });
}

function sendUserRequest(){
    var url = request_prefix + "controller/sendUser?userId=" + user_id;
    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        async: true,
        timeout: 10000,
        dataType: "json"
    });
}

function sendBookmarkRequest(entityString, relevance){
    var url = request_prefix + "controller/sendBookmark?" +
        "userId=" + user_id +
        "&taskId=" + task.id +
        "&versionId=" + task.versionId +
        "&entityString=" + entityString +
        "&relevance=" + relevance;

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        async: true,
        timeout: 10000,
        dataType: "json"
    });
}

function sendInteractionRequest(option, target){
    var url = request_prefix + "controller/sendInteraction?" +
        "userId=" + user_id +
        "&taskId=" + task.id +
        "&versionId=" + task.versionId +
        "&option=" + option +
        "&target=\"" + encodeURIComponent(target) + "\"" +
        "&queryContent=\"" + encodeURIComponent(getQueryContent()) + "\"" +
        "&timestamp=" + new Date().getTime();

    console.log(url);
    $.ajax({
        url: url,
        type: "GET",
        contentType: "application/json; charset=utf-8",
        async: true,
        timeout: 10000,
        dataType: "json"
    });
}