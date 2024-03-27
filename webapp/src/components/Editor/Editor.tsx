import {python} from "@codemirror/lang-python";
import {githubDark} from "@uiw/codemirror-theme-github";
import CodeMirror from "@uiw/react-codemirror";
import styled from "styled-components";

type EditorProps = {
  value?: string;
  onChange?: (val: string) => void;
  onSubmit: () => void;
};

const PinnedButton = styled.button`
  position: absolute;
  bottom: 1rem;
  right: 1rem;
  z-index: 1;
`;

export default function Editor({value, onChange, onSubmit}: EditorProps) {
  return (
    <>
      <PinnedButton onClick={onSubmit} disabled={false}>
        Submit
      </PinnedButton>
      <CodeMirror
        height="100%"
        value={value}
        extensions={[python()]}
        onChange={onChange}
        theme={githubDark}
        // This is a workaround to ensure the editor takes up the full space and
        // also respects the selected theme. When using `styled(CodeMirror)` the
        // theme is not applied correctly
        style={{height: "100%", width: "100%", flexGrow: 1}}
      />
    </>
  );
}
