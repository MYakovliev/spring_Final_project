function readURL(input, imageId, inputId) {
    let image;
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        image = document.getElementById(imageId)

        reader.addEventListener("load", function () {
            image.src = reader.result;
            image.css("visibility", "visible")
        }, false);

        if (inputId != null) {
            $('#' + inputId + 'Label').css('visibility', 'visible');
            $('#' + inputId + 'Input').css('visibility', 'visible');
        }

        reader.readAsDataURL(input.files[0]);
    }
}