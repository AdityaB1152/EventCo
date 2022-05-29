const axios = require("axios").default;
const apiKey = "40ac89417add49e7ae5be7eaafe81e3f";
const { json } = require("express");
const express = require("express");
const createHandler = require("azure-function-express").createHandler;
const app = express();

app.post("/api/FaceIdentification",(req,res)=>{
    console.log('JavaScript HTTP trigger function processed a request.');

    const downloadUrl = req.body.downloadUrl;
    const eventId = req.body.eventId;
    let flag;
    
    console.log("Calling the Face Detect Api")
    
axios.post("https://event-co.cognitiveservices.azure.com/face/v1.0/detect?recognitionModel=recognition_01&detectionModel=detection_01",
{
    // url:"https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRAdEFiYJ2ZnbTshnVPem8IDXRCTZNUrnHBj9eMT1iW_ryXQJCF"
    url:downloadUrl
}
,{
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
        console.log(obj);
        if(obj!=null){
           flag = "Yes";
           res.send({
               verify:true
           });
        }
        else{
          flag = "No";
          res.send({
              verify:false
          })
        }   
    }).catch((error)=>{
        console.log("2Er",error);
        res.send(error);
    });
}).catch((error)=>{
    console.log("1Er",error);
    res.send(error);
});

});

module.exports = createHandler(app);

