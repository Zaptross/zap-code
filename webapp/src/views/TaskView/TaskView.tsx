import QuadWindow from "../../components/ResizeableWindows/QuadWindow";
import Editor from "../../components/Editor/Editor";
import {useCallback, useContext, useState} from "react";
import useFetchTasks from "../../hooks/fetchTasks";
import styled from "styled-components";
import {UserProfileContext} from "../../providers/UserProfileContext/UserProfileContext";
import {Task} from "../../types/Task";
import useMutateSubmitAttempt from "../../hooks/mutateSubmitAttempt";
import useFetchAttemptsForTask from "../../hooks/fetchAttemptsForTask";
import TestsDisplay from "../../components/LinesDisplay/TestsDisplay";
import LogsDisplay from "../../components/LinesDisplay/LogsDisplay";
import useFetchAttempt from "../../hooks/fetchAttempt";

const PaddedP = styled.div`
  padding: 1.5em;
`;

const LOADING = "Loading...";
const CLOADING = `# ${LOADING}`;
const LOGS = ["Logs will appear here once you run the code!"];
const TESTS = ["Test case results will appear here once you run the code!"];

export default function TaskView() {
  const [code, setCode] = useState(CLOADING);
  const [task, setTask] = useState<Task | undefined>(undefined);
  const [attemptId, setAttemptId] = useState<string | undefined>(undefined);
  const [logs, setLogs] = useState<string[]>(LOGS);
  const [tests, setTests] = useState<string[]>(TESTS);
  const {userProfile} = useContext(UserProfileContext);
  const tasksFetch = useFetchTasks();
  const taskAttemptsFetch = useFetchAttemptsForTask(task?.id);
  const currentAttemptFetch = useFetchAttempt(attemptId);
  const submitAttempt = useMutateSubmitAttempt();

  if (tasksFetch.data && !task) {
    setTask(tasksFetch.data[0]);
  }

  if (
    submitAttempt.data?.attemptId &&
    submitAttempt.data.attemptId !== attemptId
  ) {
    setAttemptId(submitAttempt.data?.attemptId);
  }

  if (taskAttemptsFetch.data?.[0]?.id && !attemptId) {
    setAttemptId(taskAttemptsFetch.data[0].id);
  }

  if (
    attemptId &&
    currentAttemptFetch.data?.solution &&
    (code === CLOADING || (task && code === task.template))
  ) {
    setCode(currentAttemptFetch.data.solution);
  } else if (task && code === CLOADING) {
    setCode(task.template);
  }

  const onChange = useCallback((val: string) => {
    setCode(val);
  }, []);

  const onSubmit = () => {
    if (!userProfile || !task || !code) {
      return;
    }

    submitAttempt.mutate({
      solution: code,
      taskId: task?.id,
      userId: userProfile.id,
    });
  };

  return (
    <QuadWindow>
      <PaddedP>
        {tasksFetch.isPending ? LOADING : task?.description ?? LOADING}
      </PaddedP>
      <Editor value={code} onChange={onChange} onSubmit={() => onSubmit()} />
      <LogsDisplay attemptId={attemptId} logs={logs} setLogs={setLogs} />
      <TestsDisplay attemptId={attemptId} tests={tests} setTests={setTests} />
    </QuadWindow>
  );
}
