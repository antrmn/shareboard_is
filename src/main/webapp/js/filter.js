$(() =>{
    /*
     * Imposta il valore iniziale per il tasto "popular" in base ai seguenti
     * criteri, in ordine di priorità a partire dal più importante:
     * 1. Query string
     * 2. Localstorage
     * 3. Valore di default
     */
    if($("#filter").attr("filterset") !== undefined) {
        //no op. Il backend ha elaborato la query string e settato il pulsante appropriatamente
    } else if (localStorage.getItem("filterPreferences") !== null) {
        if (localStorage.getItem("filterPreferences") === "following") {
            $("#filter a#popular-button").removeClass("selected");
        } else {
            $("#filter a#popular-button").addClass("selected");
        }
    } else {
        //default
        $("#filter a#popular-button").addClass("selected");
    }

    /* Come sopra, ma per i tasti "new" e "top" */
    if($("#filter").attr("orderbyset") !== undefined) {
        //no op
    } else if (localStorage.getItem("orderByPreferences") !== null) {
        if (localStorage.getItem("orderByPreferences") === "new") {
            $("#filter a#new-button").addClass("selected");
        } else {
            $("#filter a#top-button").addClass("selected");
        }
    } else {
        //default
        $("#filter a#top-button").addClass("selected");
    }

    /**** Event handlers ****/
    $("#filter a#popular-button").click(function(){
            $(this).toggleClass("selected");
            $("#filter").trigger("filterchanged");
        }
    );
    $("#filter a#top-button, #filter a#new-button").click(function(){
        if($(this).hasClass("selected")){
            /* no op */
        } else {
            $("#filter a#top-button, #filter a#new-button").not(this).removeClass("selected");
            $(this).addClass("selected");
            $("#filter").trigger("filterchanged");
        }
    });

    $("#set-default-button").click(function(){
        if($("#filter a#popular-button").length > 0)
            localStorage.setItem("filterPreferences",
                $("#filter a#popular-button").hasClass("selected") ? "popular" : "following");
        if($("#filter a#top-button").hasClass("selected"))
            localStorage.setItem("orderByPreferences", "top");
        else
            localStorage.setItem("orderByPreferences", "new");
        alert("Preferenze impostate con successo");
    });

    $("#filter").trigger("filterchanged");
})