function isSearchFormBlank() {
    return $("#search-form [name=content]").val().trim() === "" &&
        !$("#search-form [name=onlyfollow]").is(":checked") &&
        $("#search-form [name=section]").val().trim() === "" &&
        $("#search-form [name=author]").val().trim() === "" &&
        $("#search-form [name=postedafter]").val().trim() === "" &&
        $("#search-form [name=postedbefore]").val().trim() === ""
}

$("#search-form").submit(function() {
    if(!isSearchFormBlank()) {
        $("#post-container").empty();
        postLoader.params = {};
        //il metodo serializeArray _non_ legge campi "disabled"
        $(this).serializeArray().reduce((object, param) => {
            object[param.name] = param.value;
            return object
        }, postLoader.params);
        postLoader.params.page = 1;
        postLoader.fetch({
            empty : ()=>{
                $("#post-container").empty();
                $("#post-container").append(createEmptyElement("fas fa-frown","Nessun contenuto"));
            }
        });
        postLoader.start($("#posts-delimiter").get(0));
    }
});

$("input[name=onlyfollow]").on("change", function(){
    if(this.checked)
        $("#search-form [name=section]").hide().prop("disabled", true);
    else
        $("#search-form [name=section]").show().prop("disabled", false);
})


$(() => {
    $("input[name=onlyfollow]").trigger("change");
    postLoader.callbacks.before = () => $("#posts-delimiter").addClass("animated");
    postLoader.callbacks.always = () => $("#posts-delimiter").removeClass("animated");
    if(!isSearchFormBlank())
        $("#search-form").submit();
    else
        $("#post-container").append(createEmptyElement("fas fa-search", "Cerca qualcosa"));
});