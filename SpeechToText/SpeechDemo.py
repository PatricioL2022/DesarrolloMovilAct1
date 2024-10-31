import io
from google.oauth2 import service_account
from google.cloud import speech
from flask import Flask,request
import os
from werkzeug.datastructures import FileStorage
import jsonpickle
import json

app = Flask(__name__)

@app.route("/",methods=['POST'])
def hello_world():
    
    nombreArchivo = "elmo.mp3"#request.headers.get('nombrearchivo')
    FileStorage(request.stream).save(os.path.join("C:/Users/Patricio/source/repos/AnalisisWebTareaUno/AnalisisWebTareaUno/", nombreArchivo))
    
    client_file = 'key.json'
    credentials = service_account.Credentials.from_service_account_file(client_file)
    client = speech.SpeechClient(credentials=credentials)

    # Load the audio file
    audio_file = nombreArchivo #'elmo.mp3'
    with io.open(audio_file, 'rb') as f:
        content = f.read()
        audio = speech.RecognitionAudio(content=content)

    config = speech.RecognitionConfig(
        encoding = speech.RecognitionConfig.AudioEncoding.MP3,
        sample_rate_hertz=16000,
        language_code='es-EC'
    ) 
    response = client.recognize(config=config, audio=audio)
    print(response.results)
    resultado = {'transcript': '', 'confidence': 0, 'seconds': 0}
    for result in response.results:
       alternative = result.alternatives[0]
       resultado['transcript'] = alternative.transcript
       resultado['confidence'] = alternative.confidence
       secondsProcessing = result.result_end_time
       resultado['seconds'] = secondsProcessing.seconds

    return resultado, 200