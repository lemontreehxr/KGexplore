var temp;
var showContent='';
function getProfile(id) {
    createProfile(entityList[id]);
    temp=id;
    sendInteractionRequest("Lookup-profile", entityList[id].name);
}


function createProfile(entity) {
    var queryContent = "queryEntity=" + encodeURIComponent(encodeURIComponent(entity.name));
    for(var i = 0; i < entityList.length; i ++)
        queryContent += "&queryEntities=" + encodeURIComponent(encodeURIComponent(entityList[i].name));

    getProfileRequest(queryContent).success(function (result) {
        if (result != null) {
            console.log(result);
            document.getElementById("profile-group").innerHTML =
                '<div style="width: 100%; height: 100%;">' +
                '   <p style="height: 25px; overflow-y: hidden; text-overflow: ellipsis;font-size: medium; margin: 5px 5px 0px 5px;"><a target="_blank" href="https://en.wikipedia.org/wiki/' + entity.name + '">' + entity.name + '</a></p>' +
                '   <p style="height: 30px; overflow-y: hidden; margin: 5px 5px 0px 5px;"><button type="button" class="btn btn-sm ' + (bookmark.has(entity.name) ? 'btn-secondary' : 'btn-primary') + '" style="margin: 0px;height: 100%;" id="' + entity.name + '" ' + (bookmark.has(entity.name) ? 'onclick="removeFromBookmark(this)">Remove from bookmark' : 'onclick="addToBookmark(this)">Add to bookmark') + '</button></p>' +
                '   <p style="height: 125px; overflow-y: scroll; font-size: small; word-break: break-all; margin: 5px 5px 0px 5px;">' + entity.description.content + '</p>' +
                '</div>';

            featureList = result.featureList;
            explanation = result.explanation;

            feature_page = 0;
            threshold = 0.8;
            if(task.versionId > 1) {
                createFeatureGroup();
            }
            if(task.versionId == 3) {
                createExplanationGroup();
            }
        }
    })
}

var bookmark = new Set();
function addToBookmark(obj) {
    bookmark.add(obj.id);
    sendInteractionRequest("Add-to-bookmark", obj.id);
    addToshowList(temp);
    obj.parentNode.innerHTML = '<button type="button" class="btn btn-sm btn-secondary" style="margin: 0px;height: 100%;" id="' + obj.id + '" onclick="removeFromBookmark(this)">Remove from bookmark</button>';
    if(bookmark.size == task.size) {
        createAssessGroup();
    }
}

function removeFromBookmark(obj) {
    bookmark.delete(obj.id);
    sendInteractionRequest("Remove-from-bookmark", obj.id);
    obj.parentNode.innerHTML = '<button type="button" class="btn btn-sm btn-primary" style="margin: 0px;height: 100%;" id="' + obj.id + '" onclick="addToBookmark(this)">Add to bookmark</button>';
}

function createAssessGroup() {
    var assessContent = '<div style="height: 40px; font-size: x-large; margin-bottom: 20px;">Please assess the relevance of the below entities with respect to the task!</div>';
    bookmark.forEach(function (name) {
        assessContent +=
            '<div style="font-size: large;">' + name + '</div>';

        var assess = '';
        var value = 1;
        while(value <= task.size) {
            assess +=
                '<div class="form-check form-check-inline">' +
                '   <input class="form-check-input" type="radio" name="' + name + '" style="cursor: pointer" value="' + value + '" onclick="writeAssess(this)">&nbsp;' +
                '   <label class="form-check-label">' + value + '</label>' +
                '</div>';
            value ++;
        }
        assessContent += '<div style="margin-bottom: 20px;">Confidence: ' + assess + '</div>';
    });

    assessContent += '<div style="margin: auto"><input type="submit" class="btn btn-default btn-primary" style="width: 100px" onclick="submitAssess()"></div>';
    document.getElementById("main").setAttribute("style", "display:none");
    document.getElementById("control-area").setAttribute("style", "");
    document.getElementById("control-area").innerHTML = '<div class="assess_tip">' + assessContent + '</div>';
}

/*我自己加的81-97

 */

function addToshowList(temp){

    showContent = showContent+
        '<div class="entity" id="' + encodeURIComponent(entityList[temp].name) + '" onmouseleave="recoverExplanation()" onmouseover="highlightExplanationByColumn(' + temp + ')" onclick="getProfile(' + temp + ')">' +
        '   <img class="img-thumbnail" src="'+ entityList[temp].description.image + '">' +
        '   <p>' + entityList[temp].name + '</p>' +
        '</div>';
    document.getElementById("tip-area").innerHTML =
        '<div>' + showContent + '</div>';

}

var assessedBookmark = new Set();
function writeAssess(obj) {
    assessedBookmark.add(obj.name);
    sendBookmarkRequest(obj.name, obj.value);
}

function submitAssess() {
    if(assessedBookmark.size == task.size) {
        alert("Finish this task!");
        assessedBookmark.clear();
        bookmark.clear();

        if(task_rotation_id < 3) {
            if(task.versionId == 1) {
                window.open("https://www.wjx.cn/jq/25389375.aspx");
            } else if (task.versionId == 2) {
                window.open("https://www.wjx.cn/jq/25387603.aspx");
            } else if (task.versionId == 3) {
                window.open("https://www.wjx.cn/jq/25387557.aspx");
            }
        }

        task_rotation_id ++;
        rotateTask();
    }
    else {
        alert("Please assess all entities!");
    }
}
