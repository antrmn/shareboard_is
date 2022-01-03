window.onclick = function(event) {
    if ($("#nav-crt-sctn").length && !$(event.target).is("#nav-crt-sctn") && $(event.target).parents("#nav-crt-sctn").length === 0) {
        toggleDropdown("close", "section-dropdown")
    }

    if ($("#profile-dropdown").length && !$(event.target).is("#profile-container") && $(event.target).parents("#profile-container").length === 0){
        toggleDropdown("close", "profile-dropdown")
    }

    if ($("#right-dropdown").length && !$(event.target).is("#profile-container") && $(event.target).parents("#profile-container").length === 0){
        toggleDropdown("close", "right-dropdown")
    }

    if ($("#filter-dropdown").length && !$(event.target).is("#filter-icon") && $(event.target).parents("#filter-icon").length === 0){
        toggleDropdown("close", "filter-dropdown")
    }
}

function toggleDropdown(action, id){
    // console.log(document.getElementById(id).classList.contains("show"))
    if (action === "close"){
        document.getElementById(id).classList.remove("show");
    } else if(action === "toggle"){
        document.getElementById(id).classList.toggle("show");
    }
}

function toggleFollow(e){
    let addFollow = false
    let sectionId = $(e).attr("data-section-id")

    if(!$(e).hasClass('follow-button-isfollowing')){
        addFollow = true;
    }
    $(e).toggleClass('follow-button-isfollowing')
    toggleFollowAjax(sectionId, addFollow)

    let elements = $(`.follow-button[data-section-id = "${sectionId}"]`);

    for (let element of elements){
        updateAllFollowButtons(element, addFollow)
    }

}

function toggleFollowAjax(id, addFollow){
    let url = window.location.origin+"/shareboard/unfollow";
    if(addFollow){
        url = window.location.origin+"/shareboard/follow"
    }

    $.post(url,
        {
            section: id,
        });
}

function updateAllFollowButtons(e, addFollow){
    if($(e).hasClass('fa-star')){
        toggleFollowStar(e, addFollow)
    } else if($(e).hasClass('follow-roundbutton')){
        toggleFollowButton(e, addFollow)
    }
}

$('.fa-star').on('click', function(e) {
    e.stopPropagation();
});

function toggleFollowStar(e, addFollow){
    //se la sezione non è nei preferiti
    if(!addFollow){
        e.classList.remove("fas");
        e.classList.add("far");
        e.classList.remove("favorite-star");
    }else{
        e.classList.remove("far");
        e.classList.add("fas");
        e.classList.add("favorite-star");
    }
}

function toggleFollowButton(e, addFollow){

    if(addFollow){
        $(e).removeClass("lightGreyButton")
        $(e).addClass('darkGreyButton')
        $(e).text("Joined");
    }else{
        $(e).removeClass("darkGreyButton")
        $(e).addClass('lightGreyButton')
        $(e).text("Join");
    }
}

function toggleVote(el, actiontype, elementType){
    let currentVotes = $(el).parent().find(".vote-count").text();
    let modifier = 0;
    let isAddingVote = true
    if(currentVotes === "Vote")
        currentVotes = 0;
    currentVotes = parseInt(currentVotes);
    // console.log(currentVotes);

    if(actiontype === "upvote"){
        let upvoteElement = el;
        let downvoteElement = $(el).parent().find(".downvoteIcon")

        if($(downvoteElement).hasClass('downvote-icon-active')){
            $(downvoteElement).toggleClass('downvote-icon-active')
            modifier = 2;
        } else{
            if($(upvoteElement).hasClass('upvote-icon-active')){
                isAddingVote = false;
                modifier = -1;
            } else {
                modifier = 1;
            }
        }
        $(upvoteElement).toggleClass('upvote-icon-active')
    } else if (actiontype === "downvote"){
        let upvoteElement = $(el).parent().find(".upvoteIcon");
        let downvoteElement = el;

        if($(upvoteElement).hasClass('upvote-icon-active')){
            $(upvoteElement).toggleClass('upvote-icon-active')
            modifier = -2;
        } else{
            if($(downvoteElement).hasClass('downvote-icon-active')){
                isAddingVote = false;
                modifier = 1;
            } else {
                modifier = -1;
            }
        }
        $(downvoteElement).toggleClass('downvote-icon-active')
    } else {
        console.log("ALERT ERROR IN TOGGLEVOTE")
    }

    $(el).parent().find(".vote-count").text(currentVotes + modifier);
    console.log($(el).siblings("input").val())
    let _id = $(el).siblings("input").val();
    if (isAddingVote){
        $.post(window.location.origin+"/shareboard/vote",
            {
                id: _id,
                vote: actiontype,
                type: elementType
            }).fail(function (){
            window.location.replace(window.location.origin+ "/shareboard/login")
        });
    } else {
        $.post(window.location.origin+"/shareboard/unvote",
            {
                id: _id,
                type: elementType
            }).fail(function (){
            window.location.replace(window.location.origin+ "/shareboard/login")
        });
    }
}

let getUrlParameter = function getUrlParameter(sParam) {
    let sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return typeof sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
    return false;
};


/* Responsive left/right container */
$(() => {
    let toggleView = ()=>{
        if(!window.matchMedia('(max-width: 767px)').matches)
            return;
        $("#left-container").toggleClass("selected-container");
        $("#right-container").toggleClass("selected-container");
        let x = 200;
        $(".selected-container").children().hide().each(function() {$(this).fadeIn(x); x+=200});
    }

    if ($("#left-container, #right-container").length<2){
        $("#container-switcher").remove(); //Senza i due container il tasto è inutile e va rimosso
    } else{
        $("#container-switcher").click(toggleView);
    }

})

function createEmptyElement(icon, text){
    let emptyElement = `
    <div class = "grid-x-nw justify-center align-center">
        <i class="${icon}" style = " color: rgb(215, 218, 220); font-size: 35px; margin-right: 15px;"></i>
        <h2 style = " color: rgb(215, 218, 220); font-size: 30px;">
            ${text}
        </h2>
    </div>
`
    return emptyElement;
}

function validateTextAreaById(elementId, message){
    let textarea = document.getElementById(elementId);
    return validateTextArea(textarea, message)
}

function validateTextArea(e, message){
    let textarea = e
    let pattern = new RegExp($(textarea).attr('pattern'));
    let hasError = !pattern.test($(textarea).val());
    // console.log($(textarea).val())
    textarea.setCustomValidity(hasError? message : "")
    return !hasError;
}

function validateTextAreaBySibling(e, message){
    let textarea = $(e.form).find('textarea').get(0)
    console.log(textarea)
    return validateTextArea(textarea, message)
}

function validatePassword(e){
    let form = $(e.form).get(0)
    let error = false
    if($(form).find('#pass').val() !== $(form).find('#pass2').val()){
        error = true
    }
    console.log(error)
    $(form).find('#pass2').get(0).setCustomValidity(error?"Le password devono coincidere" : "")
    return !error
}

function validateUserEdit(e){
    return validatePassword(e) && validateTextAreaBySibling(e, "Massimo 255 caratteri")
}

function openTooltip(e){
    let tooltip = document.getElementById("tooltip");
    tooltip.innerHTML = $(e).attr("data-ttp-message");
    tooltip.style.display = "block";
    tooltip.style.top = e.offsetTop - tooltip.offsetHeight - 10 + "px";
    tooltip.style.left = e.offsetLeft + "px";
}

function closeTooltip(e){
    let tooltip = document.getElementById("tooltip");
    tooltip.style.display = "none";
    tooltip.style.top = "-9999px";
    tooltip.style.left = "-9999px";
}