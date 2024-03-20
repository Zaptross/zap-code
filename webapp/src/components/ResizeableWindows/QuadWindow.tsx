import {Resizable} from "re-resizable";
import {ReactNode} from "react";
import styled from "styled-components";

type WindowHostProps = {
  children: [ReactNode, ReactNode, ReactNode, ReactNode];
};

const FillCol = styled.div`
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  border-left: 1px solid #000000aa;
`;
const FillRow = styled(FillCol)`
  flex-direction: row;
  border-top: 1px solid #000000aa;
`;
const ResizableRow = styled(Resizable)`
  display: flex;
  flex-direction: row;
`;

export default function QuadWindow({children}: WindowHostProps) {
  if (children.length !== 4) {
    throw new Error("WindowHost must have exactly 4 children");
  }

  const defaultSize = {width: "50%", height: "100%"};

  return (
    <>
      <ResizableRow
        enable={{bottom: true}}
        defaultSize={{width: "100vw", height: "50%"}}
      >
        <Resizable enable={{right: true}} defaultSize={defaultSize}>
          {children[0]}
        </Resizable>
        <FillCol style={{width: "100%", height: "100%"}}>{children[1]}</FillCol>
      </ResizableRow>
      <FillRow>
        <Resizable enable={{right: true}} defaultSize={defaultSize}>
          {children[2]}
        </Resizable>
        <FillCol>{children[3]}</FillCol>
      </FillRow>
    </>
  );
}
