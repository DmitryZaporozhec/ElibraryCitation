/**
 * Created by zaporozhec on 4/30/15.
 */
$(document).ready(function () {
    $("#doLogin").on("submit", function () {
        $.ajax({
            url: "/login",
            type: "POST",
            dataType: "text",
            contentType: "application/json",
            data: toJson(),
            success: function (data, status, jqXHR) {
                document.location.href = data;
            },
            error: function (data) {
                $("#error").text("");
                $("#error").text(data.responseText);
            }
        });
        return false;
    });
});

function toJson() {
    return JSON.stringify({login: $("#inputLogin").val(), password: $("#inputPassword").val()});

}