function toggleTextArea(el){
    let replyForm = $(el).parent().parent().find('.reply-form')[0];
    let editForm = $(el).parent().parent().find('.edit-form')[0];

    if($(el).attr("id") == "edit-button"){
        $(replyForm).hide();
        $(editForm).toggle();
    }

    if($(el).attr("id") == "reply-button"){
        $(editForm).hide();
        $(replyForm).toggle();
    }
}

function validateCommentForm(el){
    console.log($(el).find("textarea").val().length)
    if ($(el).find("textarea").val().length == 0 || $(el).children("textarea").val().length > 1000){
        return false;
    }

    return true;
}

function getPostId(){
    return $("#post-data").find('input').val();
}
