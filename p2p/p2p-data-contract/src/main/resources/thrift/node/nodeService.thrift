/*
 * Copyright (c) 2016 p2p, Inc. All rights reserved.
 */

namespace java edu.xmh.p2p.data.contract.service.node

/**
 * Node app information receiver.
 */
service TNodeService {
    list<string> queryFileUrls(1:string profile);
    list<string> queryFilePatchUrls(1:string profile);
}