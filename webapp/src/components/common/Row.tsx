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

export const HeaderRow = styled(Row)`
  width: calc(100vw - 0.5rem);
  padding: 0.25rem;
  margin: 0;
  border-left: none;
  border-right: none;
  border-top: none;
  justify-content: space-between;
`;
