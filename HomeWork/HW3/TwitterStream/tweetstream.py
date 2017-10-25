"""
tweet_read.py

Serve tweets to a socket for spark-streaming.

Adapted from:
    http://www.awesomestats.in/spark-twitter-stream/

"""

import tweepy
from tweepy import OAuthHandler
from tweepy import Stream
from tweepy.streaming import StreamListener
import socket
import json
import logging

logger = logging.getLogger("tweetread")

logging.basicConfig(level=logging.INFO)

CONSUMER_KEY = 'tOpKHcNzNpu88PSZxAzCI87Ne'
CONSUMER_SECRET = 'EhSEN89oydJi058EkQP3iMjsVlYw6yLYZ2Uq2UAVSWS43wXju9'
ACCESS_TOKEN = '37501551-hUS1bgjvyBq9H1pplnXkQb1rBIqNfNzPsBHFtx8dw'
ACCESS_SECRET = '0MI19XnM6FXT8D8LWM70KbHYZDp5GefyZpYwD6hUUvtSD'


class TweetsListener(StreamListener):

    def __init__(self, sock):
        self._sock = sock
        self._count = 0

    def on_data(self, data):
        try:
            msg = json.loads(data)
            if 'text' not in msg: return True
            text = msg['text'].encode('utf-8')
            self._count += 1
            self._sock.send(text)
            if self._count % 10 == 0:
                logger.info("Forwarded %d messages", self._count)
            return True
        except BaseException as e:
            logger.exception("Error on_data: %s", e)
        return True

    def on_error(self, status):
        logger.error("API Error status: %d", status)
        return True


def sendData(sock):
    auth = OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)
    auth.set_access_token(ACCESS_TOKEN, ACCESS_SECRET)

    twitter_stream = Stream(auth, TweetsListener(sock))
    twitter_stream.sample()

    #twitter_stream.filter(track=['bill nye'])


def main():
    s = socket.socket()         # Create a socket object
    host = "127.0.0.1"          # Get local machine name
    port = 5555                 # Reserve a port for your service.
    s.bind((host, port))        # Bind to the port

    logger.info("Listening on port: %d", port)

    s.listen(5)                 # Now wait for client connection.
    c, addr = s.accept()        # Establish connection with client.

    logger.info("Received request from: %s", addr)

    sendData(c)


if __name__ == "__main__":
    main()