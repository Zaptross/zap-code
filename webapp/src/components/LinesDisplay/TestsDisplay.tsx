import {useState} from "react";
import LinesDisplay from "./LinesDisplay";
import useFetchAttemptLogs from "../../hooks/fetchAttemptLogs";

type TestsDisplayProps = {
  attemptId?: string;
  tests: string[];
  setTests: (tests: string[]) => void;
};

export default function TestsDisplay({
  tests,
  setTests,
  attemptId,
}: TestsDisplayProps) {
  const attemptFetch = useFetchAttemptLogs(attemptId);

  if (
    attemptId &&
    attemptFetch.data &&
    !attemptFetch.isPending &&
    tests !== attemptFetch.data.tests
  ) {
    setTests(attemptFetch.data.tests);
  }

  return <LinesDisplay logs={tests} />;
}
