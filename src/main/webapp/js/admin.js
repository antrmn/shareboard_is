function openNav() {
    document.getElementById("mySidenav").style.width = "300px";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

function toggleAdminStatus(e, id){
    $.get('./toggleAdmin',
        {
            isAdmin: e.checked,
            userId: id
        }).fail(function(){
            $(e).prop("checked", true)
            alert("Azione non permessa")
    });
}

function openModal(){
    document.getElementById("myModal").style.display = "block";
}

function closeModal(){
    document.getElementById("myModal").style.display = "none";
}

window.addEventListener("click", function(event) {
    if (event.target == document.getElementById("myModal")) {
        closeModal();
    }
});


$('#ban-form').submit(function(e) {
    e.preventDefault();

    $.post('./addban', $('#ban-form').serialize())
        .done(function(){ window.location.reload(false);})
        .fail(function(xhr, status, error){
            let response = JSON.parse(xhr.responseText)
            $('#error-list').empty();
            for(let i = 0; i< response.errors.length; i++){
                $('#error-list').append(`<li>${response.errors[i]}</li>`)
            }
        });
});