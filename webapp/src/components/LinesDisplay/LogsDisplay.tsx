import LinesDisplay from "./LinesDisplay";
import useFetchAttemptLogs from "../../hooks/fetchAttemptLogs";

type LogsDisplayProps = {
  attemptId?: string;
  logs: string[];
  setLogs: (logs: string[]) => void;
};

export default function LogsDisplay({
  logs,
  setLogs,
  attemptId,
}: LogsDisplayProps) {
  const attemptFetch = useFetchAttemptLogs(attemptId);

  if (
    attemptId &&
    attemptFetch.data &&
    !attemptFetch.isPending &&
    logs !== attemptFetch.data.logs
  ) {
    setLogs(attemptFetch.data.logs);
  }

  return <LinesDisplay logs={logs} />;
}
