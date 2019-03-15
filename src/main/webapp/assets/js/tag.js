function createSpan_feature(name){
    if(checkDuplication_feature(name)) {
        var span = document.createElement("span");
        span.setAttribute("class", "tag tag-feature");
        var tokens = decodeURIComponent(name).split("##");
        span.innerHTML = tokens[0] + ":" + (tokens[2] == -1 ? '<u>' + tokens[1] + '</u>' : tokens[1]);

        var input = document.createElement("input");
        input.setAttribute("type", "text");
        input.setAttribute("name", "feature");
        input.setAttribute("value", name);
        input.style = "display:none;";

        var remove = document.createElement("span");
        remove.setAttribute("data-role", "remove");
        remove.setAttribute("onclick", "deleteSpan_feature(this)");

        span.appendChild(input);
        span.appendChild(remove);

        return span;
    }
    else
        return null;
}

function checkDuplication_feature(name){
    var flag = true;
    var features = document.getElementsByName("feature");
    for(var i = 0; i < features.length; i ++){
        if(features[i].value == name) {
            flag = false;
            alert("Duplication!!");
            break;
        }
    }
    return flag
}

function addSpan_feature(object){
    document.getElementById("input-tag-box").appendChild(createSpan_feature(object.id));
    sendInteractionRequest("Add-a-feature", object.id);
    getResult();
}

function deleteSpan_feature(object){
    document.getElementById("input-tag-box").removeChild(object.parentNode);
    sendInteractionRequest("Delete-a-feature", object.value);
    getResult();
}

function createSpan_entity(name){
    if(checkDuplication_entity(name)) {
        var span = document.createElement("span");
        span.setAttribute("class", "tag tag-entity");
        span.innerHTML = decodeURIComponent(name);

        var input = document.createElement("input");
        input.setAttribute("type", "text");
        input.setAttribute("name", "entity");
        input.setAttribute("value", name);
        input.style = "display:none;";

        var remove = document.createElement("span");
        remove.setAttribute("data-role", "remove");
        remove.setAttribute("onclick", "deleteSpan_entity(this)");

        span.appendChild(input);
        span.appendChild(remove);

        return span;
    }
    else
        return null;
}

function checkDuplication_entity(name){
    var flag = true;
    var entities = document.getElementsByName("entity");
    for(var i = 0; i < entities.length; i ++){
        if(entities[i].value == name) {
            flag = false;
            alert("Duplication!!");
            break;
        }
    }
    return flag
}

function addSpan_entity(object){
    document.getElementById("input-tag-box").appendChild(createSpan_entity(object.id));
    sendInteractionRequest("Add-an-entity", object.id);
    getResult();
}

function deleteSpan_entity(object){
    document.getElementById("input-tag-box").removeChild(object.parentNode);
    sendInteractionRequest("Delete-an-entity", object.value);
    getResult();
}