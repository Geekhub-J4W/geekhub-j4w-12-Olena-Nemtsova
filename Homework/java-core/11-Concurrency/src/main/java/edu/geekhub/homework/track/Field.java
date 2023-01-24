package edu.geekhub.homework.track;

import java.util.List;

public record Field(Block startBlock, List<Block> trackBlocks, Block finishBlock) {
}
