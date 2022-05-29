const axios = require("axios").default;


module.exports = async function (context, req) {
    context.log('JavaScript HTTP trigger function processed a request.');

    const downloadUrl = req.body.downloadUrl;
    const eventId = req.body.eventId;
    const apiKey = "40ac89417add49e7ae5be7eaafe81e3f";
    
    console.log("Calling the Face Detect Api")
    axios.post("https://event-co.cognitiveservices.azure.com/face/v1.0/detect?recognitionModel=recognition_01&detectionModel=detection_01",
    {
        url:downloadUrl
    },{
        headers:{
            "Ocp-Apim-Subscription-Key":apiKey
        }
    }).then((response)=>{
        const faceId = response.data[0].faceId;
        console.log("Face Detected with FaceId : ",faceId);
        const faceIds = [];
    faceIds.push(faceId);
    faceIds.push(faceId);
        console.log("Calling the Face Verification Api")
        axios.post("https://event-co.cognitiveservices.azure.com/face/v1.0/identify",{
            faceIds:faceIds,
            personGroupId:eventId,
            maxNumOfCandidatesReturned:1,
            confidenceThreshold:0.5
        },{
            headers:{
                "Ocp-Apim-Subscription-Key":apiKey
            }
        }).then((response1)=>{
            
            console.log("Response Recived");
            const obj = response1.data[0].candidates;
            if(candidates!=null){
                context.res =
                 {
                    status:200,
                    body:{
                           match:true,
                        }
                    }
        }
            else {
                context.res = {
                    body:{
                        match:false
                    }
                }
            }
        }).catch((error)=>{
            console.log("Error");
        });
    })

}

