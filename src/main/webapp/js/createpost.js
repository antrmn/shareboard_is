$(() => {
    if($("#text-button").hasClass("post-type-button-selected"))
        togglePostType($("#text-button")[0]);
    else if($("#image-button").hasClass("post-type-button-selected"))
        togglePostType($("#image-button")[0]);
    else
        togglePostType($("#text-button")[0]);
});

function togglePostType(e, isCreating){
    if($(e).is("#text-button")){
        $(e).addClass("post-type-button-selected");
        $("#image-button").removeClass("post-type-button-selected");
        $("#post-type").val("text");
        $("#text-field").removeAttr("hidden");
        $("#img").attr("hidden", true);
        if(isCreating)
            $("#img").attr("required", false);
    } else if ($(e).is("#image-button")){
        $(e).addClass("post-type-button-selected");
        $("#text-button").removeClass("post-type-button-selected");
        $("#post-type").val("picture");
        $("#img").removeAttr("hidden");
        $("#text-field").attr("hidden", true);
        if(isCreating)
            $("#img").attr("required", true);
    }
}