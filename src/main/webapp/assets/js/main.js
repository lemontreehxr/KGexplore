var user_id;
var assess;
var task_rotation_id;
var task;


function initial() {
    setUserId();
}

function setUserId() {
    document.body.innerHTML =
        '<div id="control-area">' +
        '   <div class="form-inline task_tip">' +
        '       <div class="form-group">' +
        '           <input class="form-control" style="width: 850px" id="user" placeholder="User Name" onkeydown="checkUser(event)">' +
        '       </div>' +
        '       <input type="submit" class="btn btn-default btn-primary" style="width: 100px" onclick="submitUserId()">' +
        '   </div>' +
        '</div>';
}

function checkUser(event){
    if(event.keyCode == 13) {
        submitUserId();
    }
}

function submitUserId() {
    if(document.getElementById("user").value != "") {
        user_id = document.getElementById("user").value;
        console.log("submit user: " + user_id);
        getAssess(user_id);
    }
    else {
        alert("Please input your user name!");
    }
}

function getAssess(user_id) {
    getAssessRequest(user_id).success(function (data) {
        if(data != null) {
            assess = data;
            console.log(assess);
            task_rotation_id = 0;
            rotateTask();
        }
    });
}
var keywords_pre;
function rotateTask() {

    if(task_rotation_id < assess.taskList.length) {
        task = assess.taskList[task_rotation_id];
        keywords_pre = "";
        task.versionId=3;

        //initial_version_3();
       // createTip();
        if(task.versionId == 1) {
            initial_version_1();
        }
        else if(task.versionId == 2) {
            initial_version_2();
        }
        else if(task.versionId == 3) {
            initial_version_3();
        }
        createTip();
    }
    else {
        sendUserRequest();
        alert("Finish all queries! Thanks very much!");
        window.open("https://www.wjx.cn/jq/25392896.aspx");
        window.location.reload();
    }

}

function initial_version_1 () {
    document.body.innerHTML =
        '<div id="control-area" style="display: none"></div>' +
        '<div id="main">' +
        '   <div id="tip-area"></div>' +
        '   <div id="input-text-area" class="input-group">' +
        '       <div id="input-text-box" class="seed-text-input">' +
        '           <input type="text" id="keywords" placeholder="Keywords" onkeydown="checkQuery(event)">' +
        '       </div>' +
        '       <div class="input-group-btn seed-text-button">' +
        '           <button class="btn btn-default btn-primary" id="search" onclick="getResult()">Search</button>' +
        '       </div>' +
        '   <div id="result-area">' +
        '       <div id="profile-group"></div>' +
        '       <div id="entity-group"></div>' +
        '       <div id="entity-function-group"></div>' +
        '   </div>' +
        '</div>';
}

function initial_version_2 () {
    document.body.innerHTML =
        '<div id="control-area" style="display: none"></div>' +
        '<div id="main">' +
        '   <div id="tip-area"></div>' +
        '   <div id="input-text-area" class="input-group">' +
        '       <div id="input-text-box" class="seed-text-input">' +
        '           <input type="text" id="keywords" placeholder="Keywords" onkeydown="checkQuery(event)">' +
        '       </div>' +
        '       <div class="input-group-btn seed-text-button">' +
        '           <button class="btn btn-default btn-primary" id="search" onclick="getResult()">Search</button>' +
        '       </div>' +
        '   <div id="input-tag-area" class="input-group">' +
        '       <div id="input-tag-box" class="seed-tag-input"></div>' +
        '   </div>' +
        '   <div id="result-area">' +
        '       <div id="profile-group"></div>' +
        '       <div id="entity-group"></div>' +
        '       <div id="entity-function-group"></div>' +
        '       <div id="feature-group"></div>' +
        '       <div id="feature-function-group" style="clear: left"></div>' +
        '   </div>' +
        '</div>';
}

function initial_version_3 () {
    document.body.innerHTML =
        '<div id="control-area" style="display: none"></div>' +
        '<div id="main">' +
        '   <div id="tip-area"></div>' +
   //     '       <div id="show-group"></div>'+
        '   <div id="input-text-area" class="input-group">' +
        '       <div id="input-text-box" class="seed-text-input">' +
        '           <input type="text" id="keywords" placeholder="Keywords" onkeydown="checkQuery(event)">' +
        '       </div>' +
        '       <div class="input-group-btn seed-text-button">' +
        '           <button class="btn btn-default btn-primary" id="search" onclick="getResult()">Search</button>' +
        '       </div>' +
        '   <div id="input-tag-area" class="input-group">' +
        '       <div id="input-tag-box" class="seed-tag-input"></div>' +
        '   </div>' +
        '   <div id="result-area">' +
        '       <div id="profile-group"></div>' +
        '       <div id="entity-group"></div>' +
        '       <div id="entity-function-group"></div>' +
        '       <div id="feature-group"></div>' +
        '       <div id="explanation-group"></div>' +
        '       <div id="explanation-function-group"></div>' +
        '       <div id="feature-function-group"></div>' +
        '       <div id="timeline"></div>' +
        '   </div>' +
        '</div>';
}

function checkQuery(event){
    if(event.keyCode == 13) {
        getResult();
    }
}

function getResult() {
    checkKeywords();
    var queryContent = getQueryContent();
    createResultGroup(queryContent);
    if(task.versionId == 3) {
        createTimeline(queryContent);
    }
}

//create query area
function checkKeywords() {
    var keywords = document.getElementById("keywords");
    if (keywords.value != "" && keywords.value != keywords_pre) {
        var entities = document.getElementsByName("entity");
        for(var i = 0; i < entities.length; i ++) {
            entities[i].parentNode.removeChild(entities[i]);
        }
        var features = document.getElementsByName("feature");
        for(var i = 0; i < features.length; i ++) {
            features[i].parentNode.removeChild(features[i]);
        }
        sendInteractionRequest("Update-keywords", keywords.value);
        keywords_pre = keywords.value;
    }
}
//得到查询内容，包括用户键入和用户勾选的特征值
function getQueryContent(){
    var queryContent = "";
    var keywords = document.getElementById("keywords");
    if (keywords.value != "") {
        queryContent += "keywords=" + encodeURIComponent(keywords.value);
    }

    var entities = document.getElementsByName("entity");
    for(var i = 0; i < entities.length; i ++) {
        if (entities[i].value != "") {
            queryContent += "&queryEntities=" + encodeURIComponent(entities[i].value);
        }
    }

    if(!queryContent.includes("queryEntities"))
        queryContent += "&queryEntities=";

    var features = document.getElementsByName("feature");
    for(var i = 0; i < features.length; i ++) {
        if (features[i].value != "") {
            queryContent += "&queryFeatures=" + encodeURIComponent(features[i].value);
        }
    }

    if(!queryContent.includes("&queryFeatures"))
        queryContent += "&queryFeatures=";

    return queryContent;
}

function createTip() {
   /* document.getElementById("tip-area").innerHTML =
        '<p style="margin: 0px">You have already assessed ' + task_rotation_id + '/' + assess.taskList.length + ' tasks</p>' +
        '<p style="margin: 0px; color: red">Task: ' + task.description  + '</p>';*/
}

function createInputGroup() {
    document.getElementById("keywords").value = query.keywords;

    var inputTagBox = document.getElementById("input-tag-box");
    if (inputTagBox != null) {
        while (inputTagBox.hasChildNodes())
            inputTagBox.removeChild(inputTagBox.firstChild);

        for (var i = 0; i < query.entityList.length; i++)
            inputTagBox.appendChild(createSpan_entity(encodeURIComponent(query.entityList[i].name)));

        for (var i = 0; i < query.featureList.length; i++)
            inputTagBox.appendChild(createSpan_feature(encodeURIComponent(query.featureList[i].entity.name + "##" + query.featureList[i].relation.name + "##" + query.featureList[i].relation.direction)));
    }
}


//create result area
var query, entityList, featureList, explanation;
var entity_page, entity_page_size, feature_page, feature_page_size, threshold;

function createResultGroup(queryContent) {
    getResultRequest(queryContent).success(function (result){
        if(result != null) {
            console.log(result);

            query = result.query;
            entityList = result.entityList;
            featureList = result.featureList;
            explanation = result.explanation;

            flash();
        }
    })
}

function flash() {
    entity_page = 0;
    entity_page_size = 5;
    feature_page = 0;
    feature_page_size = 10;
    threshold = 0.8;
    createInputGroup();
    createEntityGroup();
   // createFeatureGroup();
   // createExplanationGroup();

    if(task.versionId > 1) {
        createFeatureGroup();
    }
    if(task.versionId == 3) {
        createExplanationGroup();
    }

}

//create entity group
function createEntityGroup() {
    var entityContent = '';
    for(var i = entity_page * entity_page_size; i < ((((entity_page + 1) * entity_page_size) < entityList.length) ? (entity_page + 1) * entity_page_size : entityList.length); i ++) {
        entityContent += createEntity(entityList[i], i);
    }

    document.getElementById("entity-group").innerHTML =
        '<div>' + entityContent + '</div>';

    document.getElementById("profile-group").innerHTML = '';

    createEntityFunctionGroup();
}

function createEntity(entity, id) {
    var imageUrl = "assets/img/not_found.jpeg";
    if(entity.description != null) {
        if(entity.description.image != 'null')
            imageUrl = entity.description.image;
    }

    var content =
        '<div class="entity" id="' + encodeURIComponent(entity.name) + '" onmouseleave="recoverExplanation()" onmouseover="highlightExplanationByColumn(' + id + ')" onclick="getProfile(' + id + ')" ondblclick="selectEntity(this)">' +
        '   <img class="img-thumbnail" src="'+ imageUrl + '">' +
        '   <p>' + entity.name + '</p>' +
        '</div>';

    return content;
}

function selectEntity(object) {
    if(task.versionId > 1) {
        addSpan_entity(object);
    }
}

function createEntityFunctionGroup() {
    document.getElementById("entity-function-group").innerHTML =
        '<div class="function-horizon">' +
        '    <button class="button-horizon" disabled></button>' +
        '    <button class="button-horizon" style="cursor: pointer" onclick="prevEntityPage()">&lt;</button>' +
        '    <button class="button-horizon" style="cursor: pointer" onclick="nextEntityPage()">&gt;</button>' +
        '    <button class="button-horizon" disabled></button>' +
        '</div>';
}

function prevEntityPage() {
    entity_page = Math.max(entity_page - 1, 0);
    createEntityGroup();
    if(task.versionId == 3) {
        createExplanationGroup();
    }
}

function nextEntityPage() {
    entity_page = Math.min(entity_page + 1, (entityList.length % entity_page_size == 0) ? Math.floor(entityList.length / entity_page_size - 1) : Math.floor(entityList.length / entity_page_size));
    createEntityGroup();
    //createExplanationGroup();

    if(task.versionId == 3) {
        createExplanationGroup();
    }

}

//create feature group
function createFeatureGroup() {
    var relation2color = new Map();
    for(var i = feature_page * feature_page_size; i < ((((feature_page + 1) * feature_page_size) < featureList.length) ? (feature_page + 1) * feature_page_size : featureList.length); i ++) {
        if(!relation2color.has(featureList[i].relation.name)) {
            var color = randomColor({
                luminosity: 'light',
                format: 'hsla' // e.g. 'hsla(27, 88.99%, 81.83%, 0.6450211517512798)'
            });
            relation2color.set(featureList[i].relation.name, color);
        }
    }

    var featureContent = '';
    for(var i = feature_page * feature_page_size; i < ((((feature_page + 1) * feature_page_size) < featureList.length) ? (feature_page + 1) * feature_page_size : featureList.length); i ++) {
        featureContent += createFeature(featureList[i], i, relation2color.get(featureList[i].relation.name));
    }

    document.getElementById("feature-group").innerHTML =
        '<div>' + featureContent + '</div>';

    createFeatureFunction();
}

function createFeature(feature, id, color) {
    var flag = false;
    if(query.featureList != null) {
        for(var i = 0; i < query.featureList.length; i ++) {
            if(feature.relation.direction == query.featureList[i].relation.direction && feature.relation.name == query.featureList[i].relation.name && feature.entity.name == query.featureList[i].entity.name) {
                flag = true;
                break;
            }
        }
    }

    var content =
        '<div class="feature" style="background-color: ' + color + '" onmouseleave="recoverExplanation()" onmouseover="highlightExplanationByRow(' + id + ')">' +
        '   <p style="float:left; width: 20px"><input type="checkbox" style="cursor: pointer" name="features" id="' + encodeURIComponent(feature.entity.name + "##" + feature.relation.name + "##" + feature.relation.direction) + '" ' + (flag ? "checked" : "") + ' onclick="selectFeature(this)"></p>' +
        '   <p style="float:left; width: 215px" id="' + encodeURIComponent(feature.entity.name) + '"' + (feature.relation.name != "subject" ? ' ondblclick="selectEntity(this)"' : '') + '>' + feature.entity.name + '</p>' +
        '   <p style="float:left; width: 60px; font-size: x-small">' + (feature.relation.direction == -1 ? "<u>" : "") + feature.relation.name + (feature.relation.direction == -1 ? "</u>" : "") + '</p>' +
        '</div>';

    return content;
}

function selectFeature(object) {
    /*  if(object.hasAttribute("checked")) {
          object.removeAttribute("checked");
          var features = document.getElementsByName("feature");
          for(var i = 0; i < features.length; i ++){
              if(features[i].value == object.id) {
                  document.getElementById("input-tag-box").removeChild(features[i].parentNode);
              }
          }
          sendInteractionRequest("Delete-a-feature", object.id);
          getResult(
 */


      if(task.versionId > 1) {
          if(object.hasAttribute("checked")) {
              object.removeAttribute("checked");
              var features = document.getElementsByName("feature");
              for(var i = 0; i < features.length; i ++){
                  if(features[i].value == object.id) {
                      document.getElementById("input-tag-box").removeChild(features[i].parentNode);
                  }
              }
              sendInteractionRequest("Delete-a-feature", object.id);
              getResult();
          }
          else {
              addSpan_feature(object);
          }
      }

}

function createFeatureFunction() {
    var relationSet = new Set();

    for(var i = 0; i < featureList.length; i ++) {
        relationSet.add(featureList[i].relation.name);
    }

    var dropdownContent = '';
    relationSet.forEach(function (relation) {
        dropdownContent += '<input type="checkbox" style="cursor: pointer" name="relations" onclick="">&nbsp;' + relation + '</p>';
    });

    document.getElementById("feature-function-group").innerHTML =
        '<div class="function-vertical">' +
        /*'    <button class="button-vertical" style="font-size: small" onclick="getResult()">Refresh</button>' +*/
        '    <button class="button-vertical" disabled></button>' +
        '    <button class="button-vertical" style="cursor: pointer" onclick="prevFeaturePage()">&lt;</button>' +
        '    <button class="button-vertical" style="cursor: pointer" onclick="nextFeaturePage()">&gt;</button>' +
        '    <button class="button-vertical" disabled></button>' +
        /*'    <div class="btn-group dropup" >' +
        '       <button style="font-size: small" type="button" class="button-vertical btn btn-secondary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
        '           Filter' +
        '       </button>' +
        '       <div class="dropdown-menu" style="font-size: small">' + dropdownContent + '</div>' +
        '</div>' +*/
        '</div>';
}

/*function filterByRelation(relationSet) {
    var featureContent = '';
    for(var i = 0; i < featureList.length; i ++) {
        if(relationSet.has(featureList[i].relation.name))
            featureContent += createFeature(featureList[i]);
    }

    document.getElementById("feature-group").innerHTML =
        '<div>' + featureContent + '</div>';



    createFeatureFunction();
}*/

function prevFeaturePage() {
    feature_page = Math.max(feature_page - 1, 0);
    createFeatureGroup();
    if(task.versionId == 3) {
        createExplanationGroup();
    }
}

function nextFeaturePage() {
    feature_page = Math.min(feature_page + 1, (featureList.length % feature_page_size == 0) ? Math.floor(featureList.length / feature_page_size - 1) : Math.floor(featureList.length / feature_page_size));
    createFeatureGroup();
    if(task.versionId == 3) {
        createExplanationGroup();
    }
}

function highlightExplanationByColumn(id) {
    if(document.getElementsByClassName("explanation") != null && document.getElementsByClassName("explanation").length > 1) {
        var explanationBoxes = document.getElementsByClassName("explanation");
        for (var i = id % entity_page_size; i < explanationBoxes.length; i += entity_page_size) {
            explanationBoxes[i].setAttribute("style", explanationBoxes[i].getAttribute("style") + "border: 2px solid #ccc;border-color: red;");
        }
    }
}

function highlightExplanationByRow(id) {
    if(document.getElementsByClassName("explanation") != null && document.getElementsByClassName("explanation").length > 1) {
        var explanationBoxes = document.getElementsByClassName("explanation");
        for (var i = (id % feature_page_size) * entity_page_size; i < ((id % feature_page_size) + 1) * entity_page_size; i ++) {
            explanationBoxes[i].setAttribute("style", explanationBoxes[i].getAttribute("style") + "border: 2px solid #ccc;border-color: red;");
        }
    }
}

function recoverExplanation() {
    if(document.getElementsByClassName("explanation") != null && document.getElementsByClassName("explanation").length > 1) {
        var explanationBoxes = document.getElementsByClassName("explanation");
        for (var i = 0; i < explanationBoxes.length; i++) {
            explanationBoxes[i].setAttribute("style", explanationBoxes[i].getAttribute("style").replace("border: 2px solid #ccc;border-color: red;", ""));
        }
    }
}

function createExplanationGroup() {
    var explanationContent =  '';
    for (var i = feature_page * feature_page_size; i < ((((feature_page + 1) * feature_page_size) < featureList.length) ? (feature_page + 1) * feature_page_size : featureList.length); i++) {
        var explanationContent_row =  '';
        for(var j = entity_page * entity_page_size; j < ((((entity_page + 1) * entity_page_size) < entityList.length) ? (entity_page + 1) * entity_page_size : entityList.length); j ++) {
            explanationContent_row += createExplanation(explanation.scoreListList[i][j], i, j);
        }
        explanationContent += '<div class="explanation_row">' + explanationContent_row + '</div>'
    }

    document.getElementById("explanation-group").innerHTML =
        '<div>' + explanationContent + '</div>';

    generateColorBar();

    /*var thresholdContent = '';

    var value = 1.0;
    while(value >= 0) {
        thresholdContent +=
            '   <div class="radio">' +
            '       <label>' +
            '           <input type="radio" name="threshold" style="cursor: pointer" onclick="switchThreshold(this)" value="' + value + '"' + (value == threshold ? "checked" : "") + '>&nbsp;' + value +
            '       </label>' +
            '   </div>';
        value = (value - 0.1).toFixed(1);
    }

    document.getElementById("explanation-function-group").innerHTML =
        '<div class="threshold">' +  thresholdContent + '</div>';*/
}

var startColor = '#c9dd22';
var endColor = '#FFFFFF';//populate('#');

function generateColorBar() {
    var gradient = "linear-gradient(" + startColor + ", " + endColor + ")";

    document.getElementById("explanation-function-group").style.background = gradient;
}

function gradientColor(value) {
    var startRGB = colorToRgb(startColor);//转换为rgb数组模式
    var startR = startRGB[0];
    var startG = startRGB[1];
    var startB = startRGB[2];

    var endRGB = colorToRgb(endColor);
    var endR = endRGB[0];
    var endG = endRGB[1];
    var endB = endRGB[2];

    var sR = (endR - startR) * value;//总差值
    var sG = (endG - startG) * value;
    var sB = (endB - startB) * value;

    var hex = colorToHex('rgb('+ parseInt((endR - sR))+ ',' + parseInt((endG - sG))+ ',' + parseInt((endB - sB)) + ')');

    return hex;
}

function colorToRgb(sColor){
    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
    var sColor = sColor.toLowerCase();
    if(sColor && reg.test(sColor)){
        if(sColor.length === 4){
            var sColorNew = "#";
            for(var i=1; i<4; i+=1){
                sColorNew += sColor.slice(i,i+1).concat(sColor.slice(i,i+1));
            }
            sColor = sColorNew;
        }
        //处理六位的颜色值
        var sColorChange = [];
        for(var i=1; i<7; i+=2){
            sColorChange.push(parseInt("0x"+sColor.slice(i,i+2)));
        }
        return sColorChange;
    }else{
        return sColor;
    }
}

function colorToHex(rgb){
    var _this = rgb;
    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
    if(/^(rgb|RGB)/.test(_this)){
        var aColor = _this.replace(/(?:\(|\)|rgb|RGB)*/g,"").split(",");
        var strHex = "#";
        for(var i=0; i<aColor.length; i++){
            var hex = Number(aColor[i]).toString(16);
            hex = hex<10 ? 0+''+hex :hex;// 保证每个rgb的值为2位
            if(hex === "0"){
                hex += hex;
            }
            strHex += hex;
        }
        if(strHex.length !== 7){
            strHex = _this;
        }

        return strHex;
    }else if(reg.test(_this)){
        var aNum = _this.replace(/#/,"").split("");
        if(aNum.length === 6){
            return _this;
        }else if(aNum.length === 3){
            var numHex = "#";
            for(var i=0; i<aNum.length; i+=1){
                numHex += (aNum[i]+aNum[i]);
            }
            return numHex;
        }
    }else{
        return _this;
    }
}

function createExplanation(score, feature_id, entity_id) {
    var explanationContent =
        '<div class="explanation" style="background-color:' + gradientColor(score.toFixed(3))  + ';" onmouseleave="recoverEntity();recoverFeature()" onmouseover="highlightEntity(' + (entity_id % entity_page_size) + ');highlightFeature(' + (feature_id % feature_page_size) + ')">' +
        //'   <label>' + score.toFixed(2) + '</label>' +
        '</div>';

    return explanationContent;
}

function highlightEntity(id) {
    if(document.getElementsByClassName("entity") != null && document.getElementsByClassName("entity").length > 1) {
        var entityBoxes = document.getElementsByClassName("entity");
        entityBoxes[id].setAttribute("style", "border: 2px solid #ccc;border-color: red;");
    }
}

function highlightFeature(id) {
    if(document.getElementsByClassName("feature") != null && document.getElementsByClassName("feature").length > 1) {
        var featureBoxes = document.getElementsByClassName("feature");
        featureBoxes[id].setAttribute("style", "border: 2px solid #ccc;border-color: red;");
    }
}

function recoverEntity() {
    if(document.getElementsByClassName("entity") != null && document.getElementsByClassName("entity").length > 1) {
        var entityBoxes = document.getElementsByClassName("entity");
        for (var i = 0; i < entityBoxes.length; i++) {
            entityBoxes[i].setAttribute("style", "");
        }
    }
}

function recoverFeature() {
    if(document.getElementsByClassName("feature") != null && document.getElementsByClassName("feature").length > 1) {
        var featureBoxes = document.getElementsByClassName("feature");
        for (var i = 0; i < featureBoxes.length; i++) {
            featureBoxes[i].setAttribute("style", "");
        }
    }
}

function switchThreshold(object) {
    threshold = object.value;
    sendInteractionRequest("Switch-the-threshold", object.value);
    createExplanationGroup();
}

function createTimeline(queryContent) {
    var timelineBoxes = document.getElementsByName("timeline");
    if(timelineBoxes.length != 0 && timelineBoxes[timelineBoxes.length - 1].id == queryContent) {
        ;
    }else {
        document.getElementById("timeline").innerHTML += createQuery(queryContent);
    }
}
var nds,nd;
var temp1=0,temp2=0;
var count = 1;
function createQuery(queryContent) {
    var title = '';
    title += "Keywords: ";
    if (keywords.value != "") {
        title += keywords.value;
        nd="{id:"+temp1+"label:"+keywords.value+"}"
    }
    title += "\nEntity: ";
    var entities = document.getElementsByName("entity");
    for(var i = 0; i < entities.length; i ++) {
        if (entities[i].value != "") {
            title += "[" + decodeURIComponent(entities[i].value) + "]";
        }
    }

    title += "\nFeature: ";
    var features = document.getElementsByName("feature");
    for(var i = 0; i < features.length; i ++) {
        if (features[i].value != "") {
            title += "[" + decodeURIComponent(features[i].value) + "]";
        }
    }

    var content =
            '   <div class="radio" style="float:left;">' +
            '       <label style="height: 25px;margin: 0px; height: 25px; padding:4px; font-size: small">' +
            '           <input type="radio" style="cursor: pointer" name="timeline" id="' + queryContent + '" data-toggle="tooltip" data-placement="bottom" data-trigger="hover focus" title="' + title + '" onclick="recall(this)" checked >&nbsp;' + count ++ +
            '       </label>' +
            '   </div>';

    return content;
}

function recall(object) {
    sendInteractionRequest("Recall-a-historical-query", object.id);
    createResultGroup(object.id);
}



