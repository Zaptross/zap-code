import styled from "styled-components";

type Alignment = "center" | "flex-start" | "flex-end";
type RowProps = {
  vAlign?: Alignment;
  hAlign?: Alignment;
};

export const Row = styled.div<RowProps>`
  display: flex;
  flex-direction: row;
  align-items: ${({vAlign}) => vAlign ?? "center"};
  justify-content: ${({hAlign}) => hAlign ?? "center"};
  gap: 1rem;
  padding: 1rem;
  border: 1px solid #000;
  border-radius: 4px;
  margin: 1rem 0;
`;

export const HeaderRowHeight = "54px";
export const HeaderRow = styled(Row)`
  display: flex;
  width: calc(100% - 0.5rem);
  height: ${HeaderRowHeight};
  flex-direction: row;
  flex-wrap: nowrap;
  flex-grow: 1;
  padding: 0.25rem;
  position: absolute;
  top: 0;
  left: 0;
  margin: 0;
  border-left: none;
  border-right: none;
  border-top: none;
  justify-content: space-between;
`;
