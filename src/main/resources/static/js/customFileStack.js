// Call to API Key


// MODAL OPTIONS
    const options = {
        "url": "https://cdn.filestackcontent.com/api/file/s7tdGfE5RRKFUxwsZoYv",
        "size": 8331,
        "type": "image/png",
        "filename": "watermark.png",
        "key": "a1RyBxiglW92bS2SRmqM_watermark.png"
        };
    const client = filestack.init("A2kZypz53QySbQzGXOkTOz", options);
    console.log(client);

    const form = document.getElementById('pick-form');
    const fileInput = document.getElementById('fileupload');
    const nameBox = document.getElementById('nameBox');
    const urlBox = document.getElementById('urlBox');

    const picker = client.picker();

    // BUTTON TO UPLOAD
    const btn = document.getElementById('fs-upload');
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            picker.open();
        });


    function updateForm (result) {
        const fileData = result.filesUploaded[0];
        fileInput.value = fileData.url;

    const name = document.createTextNode('Selected: ' + fileData.filename);
    const url = document.createElement('a');
    url.href = fileData.url;
    url.appendChild(document.createTextNode(fileData.url));
    nameBox.appendChild(name);
    urlBox.appendChild(document.createTextNode('Uploaded to: '));
    urlBox.appendChild(url);
    };


    function pickMark() {
        //Opening the file picker here
        client.picker("g8fLXNCRT4K3TJSUFrRW", options).then(function(result) {
            document.getElementById("image-url").value = result.filesUploaded[0].url;
        });
    }
