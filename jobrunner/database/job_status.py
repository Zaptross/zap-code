from enum import Enum

JobStatus = Enum('JobStatus', ['PENDING', 'RUNNING', 'PASS', 'FAILED', 'COMPLETED', 'CANCELED', 'TIMEOUT', 'UNKNOWN'])