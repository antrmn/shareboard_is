/*
    Vecchio metodo per vedere se il fondo pagina è stato raggiunto
    function bottomPageReached(){
        return window.innerHeight + window.pageYOffset >= document.body.offsetHeight;
    }
*/


const postLoader = {
    lock: false,
    params: {page: 1},
    target: null,
    observer: new IntersectionObserver(function(entry){
        if (postLoader.lock === false && entry[0].isIntersecting === true)
            postLoader.fetch()
    }, {threshold: 0.1}),
    callbacks: {
        before: $.noop,
        success: (data) => $("#post-container").append(data),
        error: () => $("#post-container").append(createEmptyElement("fas fa-exclamation", "Si &egrave; verificato un errore")),
        empty: $.noop,
        always: $.noop
    },
    fetch(_callbacks = postLoader.callbacks){
        postLoader.lock = true;
        let callbacks = {};
        Object.assign(callbacks, postLoader.callbacks, _callbacks);
        if (typeof postLoader.lastRequest === "object" && typeof postLoader.lastRequest.abort === "function")
            postLoader.lastRequest.abort();
        if(!Number.isInteger(postLoader.params.page))
            postLoader.params.page = 1;
        callbacks.before();

        //setTimeout impostato solo per far vedere l'animazione di caricamento.
        setTimeout(()=>{
            this.lastRequest = $.get(window.location.origin+ "/shareboard/loadposts", postLoader.params)
                .done((data) => {
                    if(!data || data.trim() === ""){
                        callbacks.empty();
                        postLoader.stop();
                    } else {
                        callbacks.success(data);
                        this.params.page += 1;
                        //forza ricontrollo
                        this.lock = false;
                        this.observer.unobserve(this.target);
                        this.observer.observe(this.target);
                    }
                })
                .fail(() => {
                    callbacks.error();
                    postLoader.stop();
                })
                .always(() => {
                    this.callbacks.always();
                    this.lock = false;
                });
        }, 800);

    },
    start(el) {
        this.target = el;
        this.observer.observe(this.target);
    },
    stop() {
        this.observer.disconnect();
        this.lock = false;
    }
}

