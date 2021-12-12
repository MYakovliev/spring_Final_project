function readURL(input, imageId, inputId) {
    let image;
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        image = document.getElementById(imageId);
        reader.addEventListener("load", function () {
            image.src = reader.result;
            image.style = "visibility:show";
        }, false);

        if (inputId != null) {
            nextLabel = inputId + "Label";
            nextInput = inputId + "Input";
            document.getElementById(nextLabel).style = "visibility:show";
            document.getElementById(nextInput).style ="visibility:show";
        }

        reader.readAsDataURL(input.files[0]);
    }
}