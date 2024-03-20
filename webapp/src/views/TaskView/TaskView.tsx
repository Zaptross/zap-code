import QuadWindow from "../../components/ResizeableWindows/QuadWindow";
import Editor from "../../components/Editor/Editor";
import {useCallback, useState} from "react";
import useFetchTasks from "../../hooks/fetchTasks";
import styled from "styled-components";

type TaskViewProps = {};

const PaddedP = styled.div`
  padding: 1.5em;
`;

const LOADING = "Loading...";
const CLOADING = `# ${LOADING}`;

export default function TaskView({}: TaskViewProps) {
  const fetchTasks = useFetchTasks();
  const {data, error, isPending: loading} = fetchTasks();
  const [task] = data || [];
  const [code, setCode] = useState(CLOADING);
  const [logs, setLogs] = useState(LOADING);
  const [tests, setTests] = useState(LOADING);
  const onChange = useCallback((val: string) => {
    setCode(val);
  }, []);

  if (task && code === CLOADING) {
    setCode(task.template);
    setLogs("Logs will appear here once you run the code!");
    setTests("Test case logs will appear here once you run the code!");
  }

  return (
    <QuadWindow>
      <PaddedP>{loading ? "Loading..." : task.description}</PaddedP>
      <Editor value={code} onChange={onChange} />
      <PaddedP>{logs}</PaddedP>
      <PaddedP>{tests}</PaddedP>
    </QuadWindow>
  );
}
