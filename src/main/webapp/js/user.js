/**
 * Created by zaporozhec on 4/30/15.
 */
$(document).ready(function () {

    $("#edit-user-form").on("submit", function () {
        $(".alert").hide();
        $.ajax({
            url: "/user",
            type: "POST",
            dataType: "text",
            contentType: "application/json",
            data: toJson(),
            success: function (data, status, jqXHR) {
                $(".alert-success").show()
                location.reload(true);
            },
            error: function (data) {
                $(".alert-error").show()
            }
        });
        return false;
    });
});

function toJson() {
    return JSON.stringify({
        login: $("#login").val(),
        password: $("#password").val(),
        userId: $("#userId").val(),
        firstName: $("#name").val(),
        lastName: $("#lastName").val(),
        age: $("#age").val()
    });

}