package com.example.dogchallenge.utils

import okio.IOException


class NoConnectivityException : IOException()

class UnknownException : IOException()

class ApiNotResponding : IOException()